
package com.unnsvc.erhena.core;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public abstract class AbstractActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.unnsvc.erhena.core";
	private static BundleActivator plugin;
	private IWorkspace workspace;

	public AbstractActivator() {
	}

	public void start(BundleContext context) throws Exception {

		System.err.println("Starting contexgt " + context);
		super.start(context);
		plugin = this;
		workspace = ResourcesPlugin.getWorkspace();
		startBundle(context);
	}

	public abstract void startBundle(BundleContext context) throws Exception;

	public void stop(BundleContext context) throws Exception {

		stopBundle(context);
		plugin = null;
		workspace = null;
		super.stop(context);
	}

	public abstract void stopBundle(BundleContext context) throws Exception;

	public static BundleActivator getDefault() {

		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {

		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public IWorkspace getWorkspace() {

		return workspace;
	}
}
