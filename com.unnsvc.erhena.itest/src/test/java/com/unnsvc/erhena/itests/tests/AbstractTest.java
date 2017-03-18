
package com.unnsvc.erhena.itests.tests;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.Before;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.itests.Activator;

public abstract class AbstractTest {

	@Before
	public void before() {

		inject();
	}

	private void inject() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}
}
