
package com.unnsvc.erhena.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;

import com.unnsvc.erhena.wizards.service.ProjectCreationService;

public class RhenaModuleWizard extends Wizard implements INewWizard {

	private RhenaModuleWizardPage page;
	@Inject
	private ProjectCreationService projectCreation;

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
			IWorkspaceRunnable operation = new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor) throws CoreException {

					String componentName = page.getGroupName();
					String projectName = page.getProjectName();
					URI location = page.getProjectLocationURI();

					IProject created = projectCreation.createProject(componentName, projectName, location, monitor);

					for(IWorkingSet workingSet : page.getWorkingSets()) {
						
						IAdaptable[] existing = workingSet.getElements();
						IAdaptable[] newExisting = new IAdaptable[existing.length + 1];
						System.arraycopy(existing, 0, newExisting, 0, existing.length);
						newExisting[existing.length] = created;
						workingSet.setElements(newExisting);
					}
					

					// IWorkingSetManager workingSetManager =
					// PlatformUI.getWorkbench().getWorkingSetManager();
					// workingSetManager.createWorkingSet("my working set",
					// elements);
				}
			};
			workspace.run(operation, new NullProgressMonitor());

			return true;
		} catch (CoreException ce) {
			ce.printStackTrace();
		}

		return false;
	}

}
