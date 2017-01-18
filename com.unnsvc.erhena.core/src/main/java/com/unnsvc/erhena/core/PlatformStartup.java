
package com.unnsvc.erhena.core;

import java.util.Arrays;

import javax.inject.Inject;

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

import com.unnsvc.erhena.core.service.IProjectReconfigurationService;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class PlatformStartup implements IStartup {

	@Inject
	private IProjectReconfigurationService projectReconfiguration;

	@Override
	public void earlyStartup() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);

		new WorkspaceJob("Configuring workspace project classpaths") {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

				try {
					projectReconfiguration.reconfigureProjects(Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()));
				} catch (RhenaException | CoreException e) {

					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				}
				return Status.OK_STATUS;
			}
		}.schedule();

	}
}
