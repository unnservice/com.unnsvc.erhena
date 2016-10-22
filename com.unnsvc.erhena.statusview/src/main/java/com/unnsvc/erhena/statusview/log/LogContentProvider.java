
package com.unnsvc.erhena.statusview.log;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.unnsvc.rhena.core.logging.LogEvent;

public class LogContentProvider implements IStructuredContentProvider {

	private LogEvent[] logEvents;

	public LogContentProvider() {

		logEvents = new LogEvent[0];
	}

	@Override
	public Object[] getElements(Object inputElement) {

		return logEvents;
	}

	public void addElement(LogEvent event) {

		// logEvents.add(event);
		LogEvent[] evts = new LogEvent[logEvents.length + 1];
		System.arraycopy(logEvents, 0, evts, 0, logEvents.length);
		evts[evts.length - 1] = event;
		this.logEvents = evts;
	}

	public LogEvent[] getLogEvents() {

		return logEvents;
	}

}
