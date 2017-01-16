
package com.unnsvc.erhena.platform;

import org.eclipse.e4.core.di.InjectorFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.unnsvc.erhena.platform.service.IProjectService;
import com.unnsvc.erhena.platform.service.IRhenaService;
import com.unnsvc.erhena.platform.service.ProjectService;
import com.unnsvc.erhena.platform.service.RhenaService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.unnsvc.erhena.platform"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private static BundleContext context;
	// private IWorkspace workspace = ResourcesPlugin.getWorkspace();
	// private IResourceChangeListener listener;

	/**
	 * The constructor
	 */
	public Activator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@SuppressWarnings("restriction")
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		// this.context = context;
		// listener = new RhenaModuleChangeListener();
		// workspace.addResourceChangeListener(listener);

		InjectorFactory.getDefault().addBinding(IRhenaService.class).implementedBy(RhenaService.class);
		InjectorFactory.getDefault().addBinding(IProjectService.class).implementedBy(ProjectService.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		super.stop(context);
		// this.context = context;

		// if (workspace != null) {
		// workspace.removeResourceChangeListener(listener);
		// }
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {

		return plugin;
	}

	public static BundleContext getContext() {

		return context;
	}

	public static ImageDescriptor getImageDescriptor(String path) {

		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
