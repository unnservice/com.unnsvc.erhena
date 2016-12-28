
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
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.platform.service.RhenaPlatformService;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class PlatformStartup implements IStartup {

	@Inject
	private RhenaPlatformService platformService;

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

							IRhenaModule model = platformService.newWorkspaceEntryPoint(project.getLocationURI());
							IRhenaExecution execution = platformService.materialiseExecution(model);
							System.err.println("Run from phase 1");

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

				System.err.println("Register listenre");
				IWorkspace ws = ResourcesPlugin.getWorkspace();
				ws.addResourceChangeListener(new PlatformResourceChangeListener(platformService), IResourceChangeEvent.POST_CHANGE);

				return Status.OK_STATUS;
			}
		};
		phaseTwo.schedule();
	}
}
