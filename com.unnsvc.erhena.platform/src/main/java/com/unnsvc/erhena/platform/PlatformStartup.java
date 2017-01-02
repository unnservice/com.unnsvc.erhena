
package com.unnsvc.erhena.platform;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.erhena.platform.service.ProjectService;
import com.unnsvc.erhena.platform.service.RhenaService;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent.EAddRemove;

public class PlatformStartup implements IStartup {

	@Inject
	private RhenaService platformService;
	@Inject
	private ProjectService projectService;
	@Inject
	private IEventBroker eventBroker;

	@Override
	public void earlyStartup() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);

		postEarlyStartup();

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

	private void postEarlyStartup() {

		WorkspaceJob job = new WorkspaceJob("Bring projects into eRhena context") {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

				IWorkspace ws = ResourcesPlugin.getWorkspace();
				for (IProject project : ws.getRoot().getProjects()) {

					if (project.hasNature("com.unnsvc.erhena.core.nature")) {

						// project.build(IncrementalProjectBuilder.FULL_BUILD,
						// null);
						try {

							ModuleIdentifier identifier = projectService.manageProject(project.getLocationURI());
//							eventBroker.post(ErhenaConstants.TOPIC_MODULE_ADDREMOVE, new ModuleAddRemoveEvent(identifier, EAddRemove.ADDED));

							platformService.getRhenaLogger().info(PlatformStartup.class, identifier, "Brought into eRhena context");

							// Don't perform build, just manage project on
							// startup
							// IRhenaModule model =
							// projectService.newWorkspaceEntryPoint(project.getLocationURI());
							// IRhenaExecution execution =
							// platformService.materialiseExecution(model);
							// System.err.println("Run from phase 1");

						} catch (RhenaException re) {
							throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, re.getMessage(), re));
						}
					}
				}

				return Status.OK_STATUS;
			}
		};
		job.schedule();

		WorkspaceJob phaseTwo = new WorkspaceJob("Phase two, register listener") {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

				System.err.println(getClass() + ": Register listener");
				IWorkspace ws = ResourcesPlugin.getWorkspace();
				ws.addResourceChangeListener(new PlatformResourceChangeListener(platformService, projectService), IResourceChangeEvent.POST_CHANGE);

				return Status.OK_STATUS;
			}
		};
		phaseTwo.schedule();
	}
}
