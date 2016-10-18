
package com.unnsvc.erhena.statusview;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.unnsvc.erhena.common.ErhenaConstants;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class LoggingView {

	@Inject
	private IEventBroker broker;
	private LoggingViewTable tableViewerExample;

	public LoggingView() {

		System.err.println("Created");
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		System.err.println("post construct");

		Composite composite = new Composite(parent, SWT.NONE);
		tableViewerExample = new LoggingViewTable(composite);

		
//		broker.subscribe(ErhenaConstants.TOPIC_LOGEVENT, new EventHandler() {
//
//			@Override
//			public void handleEvent(Event event) {
//
//				System.err.println("on event");
//
//			}
//			
//		});
	}

	@Inject
	@Optional
	private void subscribeTopicTodoUpdated(@UIEventTopic(ErhenaConstants.TOPIC_LOGEVENT) ILoggingEvent logEvent) {

		System.err.println("on event");

		tableViewerExample.onLogEvent(logEvent);
	}

}
