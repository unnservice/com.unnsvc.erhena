package com.unnsvc.erhena.preferences.rhenaconfig;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.unnsvc.erhena.preferences.Activator;

public class RhenaBuildConfigurationPage  extends FieldEditorPreferencePage implements IWorkbenchPreferencePage  {

	public RhenaBuildConfigurationPage() {

		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Build Configuration");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

//		addField(new DirectoryFieldEditor(RhenaConfigurationConstants.P_RHENA_HOME, "&RHENA_HOME:", getFieldEditorParent()));
		addField(new BooleanFieldEditor(RhenaConfigurationConstants.P_RHENA_BUILD_PACKAGE, "&Package build", getFieldEditorParent()));
//
//		addField(new RadioGroupFieldEditor(PreferenceConstants.P_CHOICE, "An example of a multiple-choice preference", 1,
//				new String[][] { { "&Choice 1", "choice1" }, { "C&hoice 2", "choice2" } }, getFieldEditorParent()));
//		addField(new StringFieldEditor(PreferenceConstants.P_STRING, "A &text preference:", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}
}
