
package com.unnsvc.erhena.statusview;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.unnsvc.erhena.statusview.modules.AbstractModuleEntry;
import com.unnsvc.erhena.statusview.modules.AllEntry;
import com.unnsvc.erhena.statusview.modules.CoreEntry;
import com.unnsvc.erhena.statusview.modules.ModuleEntry;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.logging.LogEvent;

public class LogViewFilter extends ViewerFilter {

	private ELogLevel level;
	private AbstractModuleEntry entryType;

	public LogViewFilter(ELogLevel level) {

		this.level = level;
		this.entryType = new AllEntry();
	}

	public void setLevel(ELogLevel level) {

		this.level = level;
	}

	public void setType(AbstractModuleEntry entryType) {

		this.entryType = entryType;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		LogEvent log = (LogEvent) element;

		if (entryType instanceof AllEntry) {
			return filterLevel(log);
		} else if (entryType instanceof CoreEntry && log.getIdentifier() == null) {
			return filterLevel(log);
		} else if (entryType instanceof ModuleEntry && log.getIdentifier() != null) {
			return filterLevel(log);
		}

		return false;
	}

	private boolean filterLevel(LogEvent log) {

		return log.getLevel().isGreaterThan(level);
	}

}
