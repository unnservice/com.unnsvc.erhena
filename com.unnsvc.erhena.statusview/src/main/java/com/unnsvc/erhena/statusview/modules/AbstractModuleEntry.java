
package com.unnsvc.erhena.statusview.modules;

import com.unnsvc.rhena.common.logging.ELogLevel;

public class AbstractModuleEntry {

	private boolean activity;
	private ELogLevel level;

	public AbstractModuleEntry() {

		this.activity = false;
		this.level = ELogLevel.INFO;
	}

	public void setActivity(boolean activity) {

		this.activity = activity;
	}

	public void setLevel(ELogLevel level) {

		this.level = level;
	}

	public boolean isActivity() {

		return activity;
	}

	public ELogLevel getLevel() {

		return level;
	}

	public void reset() {

		this.activity = false;
		this.level = ELogLevel.INFO;
	}
}
