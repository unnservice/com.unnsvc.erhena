
package com.unnsvc.erhena.core.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.ErhenaUtils;
import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.core.classpath.RhenaClasspathContainerInitializer;
import com.unnsvc.erhena.core.classpath.RhenaFrameworkClasspathContainer;
import com.unnsvc.erhena.platform.service.IRhenaTransaction;
import com.unnsvc.erhena.platform.service.ProjectService;
import com.unnsvc.erhena.platform.service.RhenaService;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;

public class RhenaBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.unnsvc.erhena.core.builder.RhenaBuilder";

	@Inject
	private RhenaService platformService;
	@Inject
	private ProjectService projectService;
	
	public RhenaBuilder() {
		
		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

		IProject project = getProject();
		IJavaProject javaProject = JavaCore.create(project);

		try {
			System.err.println("Project service is " + projectService);
			platformService.newTransaction(new IRhenaTransaction() {

				@Override
				public void execute(IRhenaEngine engine) throws Throwable {

					if (kind == IncrementalProjectBuilder.FULL_BUILD) {
						// full builds

						fullBuild(javaProject, engine);

					} else if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
						// clean build

					} else {
						// incremental and auto builds
						// has delta getDelta(getProject())
						fullBuild(javaProject, engine);
					}
				}
			});
		} catch (Throwable t) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, t.getMessage(), t));
		}

		return null;
	}

	private void fullBuild(IJavaProject javaProject, IRhenaEngine engine) throws Throwable {

		ModuleIdentifier projectIdentifier = projectService.manageProject(getProject());
		IRhenaModule module = engine.materialiseModel(projectIdentifier);
		
		// Build all parents in the workspace model
		for (ModuleIdentifier root : platformService.getEngine().findRoots(projectIdentifier, EExecutionType.TEST)) {

			System.err.println("Building project " + root);

			IRhenaModule rootModule = engine.materialiseModel(root);
			IRhenaExecution exec = engine.materialiseExecution(rootModule, EExecutionType.TEST);
		}
		
		WorkspaceExecution execution = (WorkspaceExecution) engine.materialiseExecution(module, EExecutionType.TEST);
		System.err.println("Produced workspace execution: " + execution);
		
		/**
		 * Drop the model and execution after we're done?
		 */
		configureProject(javaProject, execution);
	}

	private void configureProject(IJavaProject javaProject, WorkspaceExecution execution) throws CoreException {

		// Source paths
		List<IClasspathEntry> sourcePaths = new ArrayList<IClasspathEntry>();
		for (IResource resource : execution.getInputs()) {
			System.err.println(javaProject.getProject().getName() + " Adding source folder: " + resource.getRelativePath());
			IFolder sourceFolder = getProject().getFolder(resource.getRelativePath());
			ErhenaUtils.createFolder(sourceFolder);
			IPackageFragmentRoot fragmentRoot = javaProject.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry entry = JavaCore.newSourceEntry(fragmentRoot.getPath());
			sourcePaths.add(entry);
		}

		// JVM entry
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		IPath jreContainerPath = new Path(JavaRuntime.JRE_CONTAINER);
		IPath vmPath = jreContainerPath.append(vmInstall.getVMInstallType().getId()).append(vmInstall.getName());
		IClasspathEntry jreEntry = JavaCore.newContainerEntry(vmPath);
		sourcePaths.add(jreEntry);

		//
		// for(IClasspathEntry ce : project.getRawClasspath()) {
		// System.err.println("Classpath in build: " + ce);
		// }

		IPath containerPath = new Path(RhenaClasspathContainerInitializer.CONTAINER_ID);

		RhenaClasspathContainerInitializer initializer = (RhenaClasspathContainerInitializer) JavaCore.getClasspathContainerInitializer(containerPath.segment(0));
		initializer.addClasspathEntry(JavaCore.newLibraryEntry(new Path("/home/noname/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar"), null, null));
		
		IClasspathEntry containerEntry = JavaCore.newContainerEntry(containerPath);
		sourcePaths.add(containerEntry);
		
		
		// rhena framework library
		IClasspathEntry frameworkContainer = JavaCore.newContainerEntry(new Path(RhenaFrameworkClasspathContainer.CONTAINER_ID));
		sourcePaths.add(frameworkContainer);
		

		javaProject.setRawClasspath(sourcePaths.toArray(new IClasspathEntry[sourcePaths.size()]), new NullProgressMonitor());

	}

}