
package com.unnsvc.erhena.preferences.rhenaconfig;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.unnsvc.erhena.preferences.PreferencesActivator;

public class RhenaConfigurationPreferencesInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		
		IPreferenceStore store = PreferencesActivator.getDefault().getPreferenceStore();
		store.setDefault(RhenaConfigurationConstants.P_RHENA_HOME, "/opt/rhena/latest");
		store.setDefault(RhenaConfigurationConstants.P_RHENA_BUILD_PACKAGE, true);
		// store.setDefault(PreferenceConstants.P_BOOLEAN, true);
		// store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
		// store.setDefault(PreferenceConstants.P_STRING, "Default value");
	}

}
