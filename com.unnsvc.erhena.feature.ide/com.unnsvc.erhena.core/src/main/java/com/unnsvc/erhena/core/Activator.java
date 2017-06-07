
package com.unnsvc.erhena.core;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.unnsvc.erhena.core.builder.ModuleResourceChangeListener;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.unnsvc.erhena.core";
	private static BundleActivator plugin;
	private IWorkspace workspace;
	private ModuleResourceChangeListener listener;

	public Activator() {

	}

	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		workspace = ResourcesPlugin.getWorkspace();
		listener = new ModuleResourceChangeListener();
		getWorkspace().addResourceChangeListener(listener);
	}

	public void stop(BundleContext context) throws Exception {

		getWorkspace().removeResourceChangeListener(listener);
		plugin = null;
		workspace = null;
		super.stop(context);
	}

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
