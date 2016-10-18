
package com.unnsvc.erhena.statusview.log;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.unnsvc.rhena.core.logging.LogEvent;

public class LogContentProvider implements IStructuredContentProvider {

	private List<LogEvent> logEvents;

	public LogContentProvider() {

		logEvents = new ArrayList<LogEvent>();
	}

	@Override
	public Object[] getElements(Object inputElement) {

		return logEvents.toArray();
	}

	public void addElement(LogEvent event) {

		logEvents.add(event);
	}

	public List<LogEvent> getLogEvents() {

		return logEvents;
	}

}
