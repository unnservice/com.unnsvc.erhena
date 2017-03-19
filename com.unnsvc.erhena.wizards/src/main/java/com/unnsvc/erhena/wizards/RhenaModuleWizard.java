
package com.unnsvc.erhena.wizards;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.IRhenaProject;
import com.unnsvc.erhena.wizards.service.ProjectCreationService;

public class RhenaModuleWizard extends Wizard implements INewWizard {

	private RhenaModuleWizardPage page;
	@Inject
	private ProjectCreationService projectCreation;

	public RhenaModuleWizard() {

		super();
		inject();
		setNeedsProgressMonitor(true);
	}
	
	private void inject() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
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

					IRhenaProject created = projectCreation.createProject(componentName, projectName, location, monitor);

					for(IWorkingSet workingSet : page.getWorkingSets()) {
						
						IAdaptable[] existing = workingSet.getElements();
						IAdaptable[] newExisting = new IAdaptable[existing.length + 1];
						System.arraycopy(existing, 0, newExisting, 0, existing.length);
						newExisting[existing.length] = created.getJavaProject();
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
