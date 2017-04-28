package com.unnsvc.erhena.itests;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.Before;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.itests.tests.TestPlatformSerivce;

public abstract class AbstractServiceTest {

	@Before
	public void before() throws Exception {

		BundleContext bundleContext = FrameworkUtil.getBundle(TestPlatformSerivce.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}
}
