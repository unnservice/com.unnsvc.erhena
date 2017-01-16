
package com.unnsvc.erhena.core.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.ErhenaUtils;
import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.core.classpath.RhenaClasspathContainerInitializer;
import com.unnsvc.erhena.core.classpath.RhenaFrameworkClasspathContainer;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.erhena.platform.service.IProjectService;
import com.unnsvc.erhena.platform.service.IRhenaService;
import com.unnsvc.erhena.platform.service.IRhenaTransaction;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.model.ERhenaModuleType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.Caller;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;

public class ModuleResourceChangeListener implements IResourceChangeListener {

	@Inject
	private IRhenaService platformService;
	@Inject
	private IProjectService projectService;

	public ModuleResourceChangeListener() {

		/**
		 * Inject
		 */
		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {

			return;
		}

		platformService.newTransaction(new IRhenaTransaction() {

			@Override
			public void execute(IRhenaEngine engine) throws Throwable {

				if (event.getDelta() != null) {

					ModuleDeltaTracker moduleDelta = new ModuleDeltaTracker();
					event.getDelta().accept(moduleDelta);
					if (!moduleDelta.getAffectedProjects().isEmpty()) {
						onProjects(engine, moduleDelta.getAffectedProjects());
					}
				} else if (event.getResource() != null && event.getResource().getProject() != null) {

					if (event.getResource().getName().equals(RhenaConstants.MODULE_DESCRIPTOR_FILENAME)) {
						IProject project = event.getDelta().getResource().getProject();
						if (project.hasNature(RhenaNature.NATURE_ID)) {
							onProjects(engine, Collections.singletonList(project));
						}
					}
				}
			}
		});
	}

	private void onProjects(IRhenaEngine engine, List<IProject> affectedProjects) throws RhenaException, CoreException {

		// Perform rhena transaction here over all affected projects

		System.err.println("Changed in :" + affectedProjects);

		for (IProject project : affectedProjects) {
			ModuleIdentifier id = projectService.manageProject(project);
			onModuleChange(engine, project, id);
		}
	}

	private void onModuleChange(IRhenaEngine engine, IProject project, ModuleIdentifier identifier) throws CoreException, RhenaException {

		IJavaProject javaProject = JavaCore.create(project);
		IRhenaModule module = engine.materialiseModel(identifier);
		System.err.println("Model is: " + module);
		System.err.println("Execution is: " + engine.materialiseExecution(new Caller(module, EExecutionType.TEST)));
		// assume it is always a workspace execution because the module.xml
		// change detection occurs in a workspace project
		WorkspaceExecution execution = (WorkspaceExecution) engine.materialiseExecution(new Caller(module, EExecutionType.TEST));

		/**
		 * If we've gotten this far and there are no exceptions, proceed with
		 * reconfiguring the classpath
		 * 
		 * @TODO reconfigure classpath only when something changed in the
		 *       classpath
		 */

		// Source paths
		List<IClasspathEntry> sourcePaths = new ArrayList<IClasspathEntry>();
		for (IResource resource : execution.getInputs()) {
			System.err.println(javaProject.getProject().getName() + " Adding source folder: " + resource.getRelativePath());
			IFolder sourceFolder = project.getFolder(resource.getRelativePath());
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

		RhenaClasspathContainerInitializer initializer = (RhenaClasspathContainerInitializer) JavaCore
				.getClasspathContainerInitializer(containerPath.segment(0));
		initializer.addClasspathEntry(JavaCore.newLibraryEntry(new Path("/home/noname/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar"), null, null));

		IClasspathEntry containerEntry = JavaCore.newContainerEntry(containerPath);
		sourcePaths.add(containerEntry);

		// rhena framework library
		if (module.getModuleType().equals(ERhenaModuleType.FRAMEWORK)) {
			IClasspathEntry frameworkContainer = JavaCore.newContainerEntry(new Path(RhenaFrameworkClasspathContainer.CONTAINER_ID));
			sourcePaths.add(frameworkContainer);
		}

		UIJob job = new UIJob("Setting classpath") {
			
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				
				try {
					javaProject.setRawClasspath(sourcePaths.toArray(new IClasspathEntry[sourcePaths.size()]), new NullProgressMonitor());
				} catch (JavaModelException e) {
					
					e.printStackTrace();
					return Status.CANCEL_STATUS;
				}

				return Status.OK_STATUS;
			}
		};
		job.schedule();

	}
}
