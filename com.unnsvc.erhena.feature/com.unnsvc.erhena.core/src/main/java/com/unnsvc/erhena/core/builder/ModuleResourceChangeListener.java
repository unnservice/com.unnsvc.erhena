
package com.unnsvc.erhena.core.builder;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public class ModuleResourceChangeListener implements IResourceChangeListener {

	//
	// @Inject
	// private IPlatformService platformService;
	// @Inject
	// private IProjectReconfigurationService projectReconfiguration;
	//
	// public ModuleResourceChangeListener() {
	//
	// /**
	// * Inject
	// */
	// BundleContext bundleContext =
	// FrameworkUtil.getBundle(Activator.class).getBundleContext();
	// IEclipseContext eclipseContext =
	// EclipseContextFactory.getServiceContext(bundleContext);
	// ContextInjectionFactory.inject(this, eclipseContext);
	// }
	//
	@Override
	public void resourceChanged(IResourceChangeEvent event) {

//		System.err.println("Fired resources changed");
//		try {
//
//			RhenaBuilderMonitorContext context = new RhenaBuilderMonitorContext();
//			
//			DeltaCollector coll = new DeltaCollector(event);
//			
//			for(IProject affected : coll.getProjectpaths().keySet()) {
//				
//				List<IPath> paths = coll.getProjectpaths().get(affected);
//				WorkspaceJob buildJob = new WorkspaceJob("Building " + affected.getName()) {
//
//					@Override
//					public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
//
//						
//						return null;
//					}
//				};
//				affected.build(IncrementalProjectBuilder.FULL_BUILD, context);
//			}
//			
//		} catch (CoreException e) {
//
//			e.printStackTrace();
//		}

		//
		// if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
		//
		// return;
		// }
		//
		// try {
		// if (event.getDelta() != null) {
		//
		// ModuleDeltaTracker moduleDelta = new ModuleDeltaTracker();
		// event.getDelta().accept(moduleDelta);
		// if (!moduleDelta.getAffectedProjects().isEmpty()) {
		// projectReconfiguration.reconfigureProjects(moduleDelta.getAffectedProjects());
		// }
		// } else if (event.getResource() != null &&
		// event.getResource().getProject() != null) {
		//
		// if
		// (event.getResource().getName().equals(RhenaConstants.MODULE_DESCRIPTOR_FILENAME))
		// {
		// IProject project = event.getDelta().getResource().getProject();
		// if (project.hasNature(RhenaNature.NATURE_ID)) {
		// projectReconfiguration.reconfigureProjects(Collections.singletonList(project));
		// }
		// }
		// }
		// } catch (Exception ex) {
		//
		// try {
		// platformService.getRhenaLogger().fireLogEvent(ELogLevel.ERROR,
		// getClass(), null, ex.getMessage(), ex);
		// } catch (RhenaException e) {
		//
		// new UIJob("Exception in rhena module change listener") {
		//
		// @Override
		// public IStatus runInUIThread(IProgressMonitor monitor) {
		//
		// return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
		// ex.getMessage(), e);
		// }
		// }.schedule();
		// }
		// }
		//
	}

}
