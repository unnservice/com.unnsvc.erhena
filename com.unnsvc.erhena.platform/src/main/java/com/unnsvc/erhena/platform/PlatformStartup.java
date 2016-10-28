
package com.unnsvc.erhena.platform;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
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

		IWorkspace ws = ResourcesPlugin.getWorkspace();

		WorkspaceJob job = new WorkspaceJob("Bring projects into eRhena context") {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

				for (IProject project : ws.getRoot().getProjects()) {

					if (project.hasNature("com.unnsvc.erhena.core.nature")) {

						project.build(IncrementalProjectBuilder.FULL_BUILD, null);
					}
				}
				
				ws.addResourceChangeListener(new PlatformResourceChangeListener(platformService), IResourceChangeEvent.POST_CHANGE);
				
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}
