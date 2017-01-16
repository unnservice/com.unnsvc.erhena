
package com.unnsvc.erhena.core;

import org.osgi.framework.BundleContext;

import com.unnsvc.erhena.core.builder.ModuleResourceChangeListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivator {

	private ModuleResourceChangeListener listener;

	public Activator() {

	}

	@Override
	public void startBundle(BundleContext context) throws Exception {

		System.err.println("Activating");
		listener = new ModuleResourceChangeListener();
		getWorkspace().addResourceChangeListener(listener);
	}

	@Override
	public void stopBundle(BundleContext context) throws Exception {

		getWorkspace().removeResourceChangeListener(listener);
	}
}
