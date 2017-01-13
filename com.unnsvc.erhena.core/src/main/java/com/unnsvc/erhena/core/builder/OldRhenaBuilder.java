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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
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
import com.unnsvc.erhena.platform.service.ProjectService;
import com.unnsvc.erhena.platform.service.RhenaService;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.lifecycle.IResource;

public class OldRhenaBuilder extends IncrementalProjectBuilder {
	// @Inject
	// private IEventBroker eventBroker;
	// @Inject
	// private IRhenaService rhenaService;
	@Inject
	private RhenaService platformService;
	@Inject
	private ProjectService projectService;

	public OldRhenaBuilder() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {



		return null;
	}

	private void build(IProject p, IProgressMonitor monitor) throws RhenaException, CoreException {

		ModuleIdentifier identifier = projectService.manageProject(p.getLocationURI());
		IRhenaExecution testExecution = platformService.buildProject(identifier);

		// If build succeeds, continue with reconfiguring project classpaths

		IRhenaEngine engine = platformService.getEngine();
		IRhenaCache cache = engine.getContext().getCache();

		ILifecycle lifecycle = cache.getLifecycles().get(identifier);
		IExecutionContext context = lifecycle.getContext();
		List<IProcessor> processors = lifecycle.getProcessors();
		List<IResource> resources = context.getResources();

		IJavaProject project = JavaCore.create(p);
		// project.setRawClasspath(new IClasspathEntry[] {}, new
		// NullProgressMonitor());

		// Source paths
		List<IClasspathEntry> sourcePaths = new ArrayList<IClasspathEntry>();
		for (IResource resource : resources) {
			IFolder sourceFolder = p.getFolder(resource.getRelativePath());
			ErhenaUtils.createFolder(sourceFolder);
			IPackageFragmentRoot fragmentRoot = project.getPackageFragmentRoot(sourceFolder);
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
		

		project.setRawClasspath(sourcePaths.toArray(new IClasspathEntry[sourcePaths.size()]), new NullProgressMonitor());

	}

}

// initializer.initialize(containerPath, project);
// RhenaClasspathContainer container = new
// RhenaClasspathContainer(containerPath);
// container.addEntry(JavaCore.newLibraryEntry(new
// Path("/home/noname/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar"),
// null, null));
// initializer.requestClasspathContainerUpdate(containerPath, project,
// container);

// IClasspathContainer container =
// JavaCore.getClasspathContainer(containerPath, project);

// Dependencies here

// p.refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, new
// NullProgressMonitor());
// p.getFolder(".classpath").refreshLocal(org.eclipse.core.resources.IResource.DEPTH_ZERO,
// monitor);
// project.refreshLocal(org.eclipse.core.resources.IResource.DEPTH_INFINITE, new
// NullProgressMonitor());

// File projectLocation = new File(project.getLocationURI());
// System.err.println(getClass().getName() + "Full build on: " +
// projectLocation);
//
// try {
// IRhenaModule module =
// platformService.newWorkspaceEntryPoint(project.getName());
//
// // This is not cached as it bypasses the rhena context
// IRhenaExecution exec =
// module.getRepository().materialiseExecution(module,
// EExecutionType.PROTOTYPE);
//
//// IRhenaExecution exec =
// platformService.materialiseExecution(module);
// } catch (RhenaException re) {
// throw new CoreException(new Status(IStatus.ERROR,
// Activator.PLUGIN_ID, re.getMessage(), re));
// }

// private void handleResources(IProject project) throws RhenaException {

// RhenaConfiguration config = new RhenaConfiguration();
//
// IRhenaContext resolver = new CachingResolutionContext(config);
// resolver.getRepositories().add(new WorkspaceRepository(resolver, new
// File(ResourcesPlugin.getWorkspace().getRoot().getLocationURI())));
//
// ModuleIdentifier entryPointIdentifier =
// ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1");
// IRhenaModule model = resolver.materialiseModel(entryPointIdentifier);
//
// EExecutionType type = EExecutionType.PROTOTYPE;
// IRhenaEdge entryPointEdge = new RhenaEdge(type, model,
// TraverseType.SCOPE);
// GraphResolver graphResovler = new GraphResolver(resolver);
// graphResovler.resolveReferences(entryPointEdge);
//
// new
// ParallelGraphProcessor(resolver).processEdges(resolver.getEdges());
// }
// eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, data)

// try {
// handleResources(getProject());
// } catch (RhenaException re) {
// throw new CoreException(new Status(IStatus.ERROR,
// Activator.PLUGIN_ID, re.getMessage(), re));
// }

// try {
// IRhenaModule module = rhenaService.newEntryPoint("com.test",
// "com.test2", "0.0.1");
// } catch (RhenaException re) {
// re.printStackTrace();
// throw new CoreException(new Status(IStatus.ERROR,
// Activator.PLUGIN_ID, re.getMessage(), re));
// }

// IRhenaModule model =
// context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1"));
//
// EExecutionType type = EExecutionType.PROTOTYPE;
// IRhenaEdge entryPointEdge = new RhenaEdge(type, model,
// TraverseType.SCOPE);
//
// GraphResolver graphResovler = new GraphResolver(context);
// graphResovler.resolveReferences(entryPointEdge);
//
// new ParallelGraphProcessor(context).processEdges(context.getEdges());


//IResourceDelta delta = getDelta(getProject());
//if(delta != null) {
//	
//	delta.accept(new IResourceDeltaVisitor() {
//		
//		@Override
//		public boolean visit(IResourceDelta delta) throws CoreException {
//			
//			
//			System.err.println("Changed: " + delta.getResource());
//			return true;
//		}
//	});
//} else {
//
//	System.err.println(getClass() + " BUILD " + delta);
//}

//if (kind == FULL_BUILD) {
//	IProject project = getProject();
//
//	try {
//		build(project, monitor);
//	} catch (Exception e) {
//		try {
//			ModuleIdentifier identifier = projectService.manageProject(project.getLocationURI());
//			platformService.getRhenaLogger().error(getClass(), identifier, e.getMessage());
//			e.printStackTrace();
//		} catch (RhenaException re) {
//			throw new CoreException(new Status(IStatus.OK, Activator.PLUGIN_ID, re.getMessage(), re));
//		}
//	}
//}
//
//UIJob j = new UIJob("test") {
//	
//	@Override
//	public IStatus runInUIThread(IProgressMonitor monitor) {
//		
//		return null;
//	}
//};
//j.schedule();

