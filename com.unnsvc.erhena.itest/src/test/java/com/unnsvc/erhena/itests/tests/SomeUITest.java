
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.itests.Activator;
import com.unnsvc.erhena.platform.service.IRhenaService;


public class SomeUITest {
	
	@Inject
	private IRhenaService platformService;

	@Test
	public void someTest() throws Exception {
		
		inject();

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		Assert.assertNotNull(ws);
		
		Assert.assertNotNull(platformService);
	}

	private void inject() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}
}
