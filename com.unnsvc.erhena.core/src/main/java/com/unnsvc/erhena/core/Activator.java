
package com.unnsvc.erhena.core;

import org.eclipse.e4.core.di.InjectorFactory;
import org.osgi.framework.BundleContext;

import com.unnsvc.erhena.core.builder.ModuleResourceChangeListener;
import com.unnsvc.erhena.core.service.IProjectReconfigurationService;
import com.unnsvc.erhena.core.service.ProjectReconfigurationService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractActivator {

	private ModuleResourceChangeListener listener;

	public Activator() {

	}

	@Override
	public void startBundle(BundleContext context) throws Exception {

		InjectorFactory.getDefault().addBinding(IProjectReconfigurationService.class).implementedBy(ProjectReconfigurationService.class);
		listener = new ModuleResourceChangeListener();
		getWorkspace().addResourceChangeListener(listener);
	}

	@Override
	public void stopBundle(BundleContext context) throws Exception {

		getWorkspace().removeResourceChangeListener(listener);
	}
}
