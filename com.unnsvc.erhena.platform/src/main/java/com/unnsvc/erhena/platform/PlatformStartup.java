
package com.unnsvc.erhena.platform;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.platform.service.RhenaPlatformService;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class PlatformStartup implements IStartup {

	@Inject
	private RhenaPlatformService platformService;

	@Override
	public void earlyStartup() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		for (IProject project : ws.getRoot().getProjects()) {

			try {
				IRhenaModule module = platformService.newWorkspaceEntryPoint(project.getName());
			} catch (Exception ex) {
				/**
				 * @TODO show dialog instead of throwing this in the background
				 */
				throw new RuntimeException(ex.getMessage(), ex);
			}
		}

	}
}
