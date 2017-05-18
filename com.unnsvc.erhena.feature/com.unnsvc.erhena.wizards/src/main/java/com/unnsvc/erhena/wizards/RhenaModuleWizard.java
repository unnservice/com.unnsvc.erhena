
package com.unnsvc.erhena.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;

import com.unnsvc.erhena.common.InjectionHelper;
import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IProjectService;

public class RhenaModuleWizard extends Wizard implements INewWizard {

	private RhenaModuleWizardPage page;
	@Inject
	protected IProjectService projectCreationService;

	public RhenaModuleWizard() {

		super();
		InjectionHelper.inject(Activator.class, this);
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

		// WorkspaceJob createProject = new WorkspaceJob("Create project " +
		// page.getGroupName() + "." + page.getProjectName()) {
		//
		// @Override
		// public IStatus runInWorkspace(IProgressMonitor monitor) throws
		// CoreException {
		//
		// try {
		// String componentName = page.getGroupName();
		// String projectName = page.getProjectName();
		// URI location = page.getProjectLocationURI();
		//
		// IProject project =
		// projectCreationService.createRhenaProject(componentName, projectName,
		// location, monitor);
		//
		// } catch (ErhenaException ee) {
		//
		// return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
		// ee.getMessage(), ee);
		// }
		// return Status.OK_STATUS;
		// }
		// };
		// createProject.schedule();

		try {
			String componentName = page.getGroupName();
			String projectName = page.getProjectName();
			URI location = page.getProjectLocationURI();

			IProject project = projectCreationService.createRhenaProject(componentName, projectName, location, new NullProgressMonitor());
			IJavaProject jProject = JavaCore.create(project);
			/*
			 * Add to working set
			 */
			for (IWorkingSet workingSet : page.getWorkingSets()) {

				IAdaptable[] existing = workingSet.getElements();
				IAdaptable[] newExisting = new IAdaptable[existing.length + 1];
				System.arraycopy(existing, 0, newExisting, 0, existing.length);
				newExisting[existing.length] = jProject;
				workingSet.setElements(newExisting);
			}

		} catch (

		ErhenaException ee) {

			return false;
		}

		return true;

		// IRhenaProject created =
		// projectCreationService.createProject(componentName,
		// projectName, location, monitor);
		//
		// for (IWorkingSet workingSet : page.getWorkingSets()) {
		//
		// IAdaptable[] existing = workingSet.getElements();
		// IAdaptable[] newExisting = new IAdaptable[existing.length +
		// 1];
		// System.arraycopy(existing, 0, newExisting, 0,
		// existing.length);
		// newExisting[existing.length] = created.getJavaProject();
		// workingSet.setElements(newExisting);
		// }

		// IWorkingSetManager workingSetManager =
		// PlatformUI.getWorkbench().getWorkingSetManager();
		// workingSetManager.createWorkingSet("my working set",
		// elements);
		// }
		// };
		//
		// try {
		// workspace.run(operation, new NullProgressMonitor());
		// return true;
		// } catch (CoreException ce) {
		// ce.printStackTrace();
		// return false;
		// }

	}

}
