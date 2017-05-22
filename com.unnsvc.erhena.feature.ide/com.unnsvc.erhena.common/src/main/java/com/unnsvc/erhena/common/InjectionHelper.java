
package com.unnsvc.erhena.common;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

public class InjectionHelper {

	public static void inject(Class<?> contextType, Object injectable) {

		// alternative method that allows to configure services
		// IEclipseContext context = EclipseContextFactory.create();
		// // prepare the context for the test
		// context.set("myvalue1", "For testing");
		// // more things, for example a LayoutManager
		// MyClass test = ContextInjectionFactory.make(MyClass.class, context);

		BundleContext bundleContext = FrameworkUtil.getBundle(contextType).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(injectable, eclipseContext);
	}
}
