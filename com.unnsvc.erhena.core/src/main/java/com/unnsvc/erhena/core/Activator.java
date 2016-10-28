
package com.unnsvc.erhena.core;

import javax.inject.Inject;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.core.nature.RhenaModuleChangeListener;
import com.unnsvc.erhena.platform.service.RhenaPlatformService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivator {

	@Inject
	private RhenaPlatformService platformService;
	private IResourceChangeListener listener;

	public Activator() {

	}

	@Override
	public void startBundle(BundleContext context) throws Exception {
		System.err.println("Starting bundle " + context);

		/**
		 * Inject
		 */
		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);

//		/**
//		 * All workspace projects become models on startup so we capture them
//		 * all in a live context?
//		 */
//		for (IProject project : getWorkspace().getRoot().getProjects()) {
//			if (project.hasNature(RhenaNature.NATURE_ID)) {
//
//				IRhenaModule module = platformService.newWorkspaceEntryPoint(project.getName());
//				System.err.println("Created module context for: " + module.getModuleIdentifier());
//			}
//		}

//		listener = new RhenaModuleChangeListener(platformService);
//		getWorkspace().addResourceChangeListener(listener);
	}

	@Override
	public void stopBundle(BundleContext context) throws Exception {

//		getWorkspace().removeResourceChangeListener(listener);
	}
}
