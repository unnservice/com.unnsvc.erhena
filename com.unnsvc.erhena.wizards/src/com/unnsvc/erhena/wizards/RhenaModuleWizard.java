
package com.unnsvc.erhena.wizards;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class RhenaModuleWizard extends Wizard implements INewWizard {

	private RhenaModuleWizardPage page;

	public RhenaModuleWizard() {

		super();
		setNeedsProgressMonitor(true);

	}

	@Override
	public void addPages() {

		page = new RhenaModuleWizardPage();
		addPage(page);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {

		String componentName = page.getGroupName();
		String projectName = page.getProjectName();
		URI location = page.getProjectLocationURI();

		try {
			RhenaModuleProjectSupport.createProject(componentName, projectName, location);

			return true;
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
		
		return false;
	}

}
