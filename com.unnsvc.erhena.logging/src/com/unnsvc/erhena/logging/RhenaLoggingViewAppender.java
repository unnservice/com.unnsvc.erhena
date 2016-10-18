
package com.unnsvc.erhena.logging;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class RhenaLoggingViewAppender extends AppenderBase<ILoggingEvent> {
	
	@Inject
	private IEventBroker eventBroker;

	@Override
	protected void append(ILoggingEvent evt) {

		System.err.println("RECV LOGGING EVENT " + evt);

		// BundleContext bundleContext = Activator.getContext();

		BundleContext bundleContext = FrameworkUtil.getBundle(com.unnsvc.erhena.platform.service.IRhenaPlatformService.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);

		System.err.println("Eclipse context in logging fragment: " + eclipseContext);

		ContextInjectionFactory.inject(this, eclipseContext);
		
		
		eventBroker.post("com/unnsvc/erhena/logging", evt);

	}

}
