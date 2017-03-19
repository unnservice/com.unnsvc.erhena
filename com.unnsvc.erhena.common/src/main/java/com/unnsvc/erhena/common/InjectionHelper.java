
package com.unnsvc.erhena.common;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class InjectionHelper {

	public static void inject(Class<?> contextType, Object injectable) {

		BundleContext bundleContext = FrameworkUtil.getBundle(contextType).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(injectable, eclipseContext);
	}
}
