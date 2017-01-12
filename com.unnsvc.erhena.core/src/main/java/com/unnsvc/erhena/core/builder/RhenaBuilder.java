
package com.unnsvc.erhena.core.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class RhenaBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.unnsvc.erhena.core.builder.RhenaBuilder";

//	@Inject
//	private RhenaService platformService;
//	@Inject
//	private ProjectService projectService;
	
	public RhenaBuilder() {
		
//		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
//		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
//		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

		System.err.println("Running empty builder on " + getProject().getName());
		return null;
	}

//	protected IProject[] build2(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
//
//		IProject project = getProject();
//		IJavaProject javaProject = JavaCore.create(project);
//
//		try {
//			System.err.println("Project service is " + projectService);
//			platformService.newTransaction(new IRhenaTransaction() {
//
//				@Override
//				public void execute(IRhenaEngine engine) throws Throwable {
//
//					if (kind == IncrementalProjectBuilder.FULL_BUILD) {
//						// full builds
//
//						fullBuild(javaProject, engine);
//
//					} else if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
//						// clean build
//
//					} else {
//						// incremental and auto builds
//						// has delta getDelta(getProject())
//						fullBuild(javaProject, engine);
//					}
//				}
//			});
//		} catch (Throwable t) {
//			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, t.getMessage(), t));
//		}
//
//		return null;
//	}

//	private void fullBuild(IJavaProject javaProject, IRhenaEngine engine) throws Throwable {
//
//		ModuleIdentifier projectIdentifier = projectService.manageProject(getProject());
//
//		Set<ModuleIdentifier> roots = new HashSet<ModuleIdentifier>();
//		roots.addAll(platformService.getEngine().findRoots(projectIdentifier, EExecutionType.TEST));
//
//		// ...
//
//		System.err.println("Transaction==============");
//		for (ModuleIdentifier root : roots) {
//
//			System.err.println("Building project " + root);
//
//			IRhenaModule module = engine.materialiseModel(root);
//			IRhenaExecution exec = engine.materialiseExecution(module, EExecutionType.TEST);
//		}
//	}

}