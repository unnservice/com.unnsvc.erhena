
package com.unnsvc.erhena.platform;

import org.eclipse.ui.IStartup;

public class PlatformStartup implements IStartup {

//	@Inject
//	private RhenaService platformService;
//	@Inject
//	private ProjectService projectService;
//	@Inject
//	private IEventBroker eventBroker;

	@Override
	public void earlyStartup() {

//		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
//		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
//		ContextInjectionFactory.inject(this, eclipseContext);

//		postEarlyStartup();

		// for (IProject project : ws.getRoot().getProjects()) {
		//
		// try {
		// IRhenaModule module =
		// platformService.newWorkspaceEntryPoint(project.getName());
		// } catch (Exception ex) {
		// /**
		// * @TODO show dialog instead of throwing this in the background
		// */
		// throw new RuntimeException(ex.getMessage(), ex);
		// }
		// }

	}
//
//	private void postEarlyStartup() {
//
//		WorkspaceJob job = new WorkspaceJob("Bring projects into eRhena context") {
//
//			@Override
//			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
//
//				IWorkspace ws = ResourcesPlugin.getWorkspace();
//				for (IProject project : ws.getRoot().getProjects()) {
//
//					if (project.hasNature("com.unnsvc.erhena.core.nature")) {
//
//						try {
//
//							ModuleIdentifier identifier = projectService.manageProject(project.getLocationURI());
//
//							platformService.getRhenaLogger().info(PlatformStartup.class, identifier, "Brought into eRhena context");
//
//						} catch (RhenaException re) {
//							throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, re.getMessage(), re));
//						}
//
//						new WorkspaceJob("Building " + project.getName()) {
//
//							@Override
//							public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
//
//								project.build(IncrementalProjectBuilder.FULL_BUILD, null);
//
//								return new Status(IStatus.OK, Activator.PLUGIN_ID, "Built");
//							}
//						}.schedule();
//
//					}
//				}
//
//				return Status.OK_STATUS;
//			}
//		};
//		job.schedule();
//
//		WorkspaceJob phaseTwo = new WorkspaceJob("Phase two, register listener") {
//
//			@Override
//			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
//
//				System.err.println(getClass() + ": Register workspace listener");
//				IWorkspace ws = ResourcesPlugin.getWorkspace();
//				ws.addResourceChangeListener(new PlatformResourceChangeListener(platformService, projectService), IResourceChangeEvent.POST_CHANGE);
//
//				return Status.OK_STATUS;
//			}
//		};
//		phaseTwo.schedule();
//	}
}
