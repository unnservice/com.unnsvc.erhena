
package com.unnsvc.erhena.core;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.unnsvc.erhena.core.nature.RhenaModuleChangeListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.unnsvc.erhena.core";
	private static Activator plugin;

	private IWorkspace workspace = ResourcesPlugin.getWorkspace();
	private IResourceChangeListener listener;

	public Activator() {
	}

	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		listener = new RhenaModuleChangeListener();
		workspace.addResourceChangeListener(listener);
	}

	public void stop(BundleContext context) throws Exception {

		plugin = null;
		super.stop(context);

		if (workspace != null) {
			workspace.removeResourceChangeListener(listener);
		}

	}

	public static Activator getDefault() {

		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {

		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
