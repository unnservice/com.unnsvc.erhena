
package com.unnsvc.erhena.wizards;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
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

		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final IProject project = workspace.getRoot().getProject("My Project");
			IWorkspaceRunnable operation = new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor) throws CoreException {

					String componentName = page.getGroupName();
					String projectName = page.getProjectName();
					URI location = page.getProjectLocationURI();

					RhenaModuleProjectSupport.createProject(componentName, projectName, location);
				}
			};
			workspace.run(operation, null);

			return true;
		} catch (CoreException ce) {
			ce.printStackTrace();
		}

		return false;
	}

}
