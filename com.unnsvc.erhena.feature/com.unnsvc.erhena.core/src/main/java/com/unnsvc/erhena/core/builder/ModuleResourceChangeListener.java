
package com.unnsvc.erhena.core.builder;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

public class ModuleResourceChangeListener implements IResourceChangeListener {
//
//	@Inject
//	private IPlatformService platformService;
//	@Inject
//	private IProjectReconfigurationService projectReconfiguration;
//
//	public ModuleResourceChangeListener() {
//
//		/**
//		 * Inject
//		 */
//		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
//		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
//		ContextInjectionFactory.inject(this, eclipseContext);
//	}
//
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
//
//		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
//
//			return;
//		}
//
//		try {
//			if (event.getDelta() != null) {
//
//				ModuleDeltaTracker moduleDelta = new ModuleDeltaTracker();
//				event.getDelta().accept(moduleDelta);
//				if (!moduleDelta.getAffectedProjects().isEmpty()) {
//					projectReconfiguration.reconfigureProjects(moduleDelta.getAffectedProjects());
//				}
//			} else if (event.getResource() != null && event.getResource().getProject() != null) {
//
//				if (event.getResource().getName().equals(RhenaConstants.MODULE_DESCRIPTOR_FILENAME)) {
//					IProject project = event.getDelta().getResource().getProject();
//					if (project.hasNature(RhenaNature.NATURE_ID)) {
//						projectReconfiguration.reconfigureProjects(Collections.singletonList(project));
//					}
//				}
//			}
//		} catch (Exception ex) {
//
//			try {
//				platformService.getRhenaLogger().fireLogEvent(ELogLevel.ERROR, getClass(), null, ex.getMessage(), ex);
//			} catch (RhenaException e) {
//
//				new UIJob("Exception in rhena module change listener") {
//
//					@Override
//					public IStatus runInUIThread(IProgressMonitor monitor) {
//
//						return new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage(), e);
//					}
//				}.schedule();
//			}
//		}
//
	}

}
