
package com.unnsvc.erhena.statusview;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.logging.LogEvent;

public class LoggingViewFilter extends ViewerFilter {

	private ModuleIdentifier filterOn;
	private ELogLevel level;

	public LoggingViewFilter() {

		this.level = ELogLevel.INFO;
	}

	public void setFilterOn(ModuleIdentifier filterOn) {

		this.filterOn = filterOn;
	}

	public void setLevel(ELogLevel level) {

		this.level = level;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		LogEvent log = (LogEvent) element;

		// filter out those with a lower priority
		if (log.getLevel().compareTo(level) == -1) {
			return false;
		}

		if (filterOn == null) {
			if (log.getIdentifier() == null) {
				return true;
			}
		} else if (log.getIdentifier() != null) {
			if (log.getIdentifier().equals(filterOn)) {
				return true;
			}
		}
		return false;
	}
}
