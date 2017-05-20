
package com.unnsvc.erhena.wizards.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IProjectService;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.rhena.common.RhenaConstants;

@Component(service = IProjectService.class)
public class ProjectService extends AbstractProjectCreationService implements IProjectService {

	@Override
	public IProject createProject(String projectName, IProgressMonitor monitor) throws ErhenaException {

		try {
			URI path = new URI(ResourcesPlugin.getWorkspace().getRoot().getLocationURI().toString() + "/" + projectName);
			return createProject(projectName, path, monitor);
		} catch (URISyntaxException use) {
			throw new ErhenaException(use);
		}
	}

	@Override
	public IProject createRhenaProject(String component, String module, URI location, IProgressMonitor monitor) throws ErhenaException {

		String projectName = component + "." + module;

		IProject project = createProject(projectName, monitor);
		try {
			// create default descriptor
			IFile moduleDescriptor = project.getFile(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			if (!moduleDescriptor.exists()) {

				moduleDescriptor.create(getModuleTemplate(component, module), false, monitor);

				/**
				 * Configure nature
				 */
				IProjectDescription description = project.getDescription();
				
				// validate the natures
				String[] natures = new String[] { JavaCore.NATURE_ID, RhenaNature.NATURE_ID };
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IStatus status = workspace.validateNatureSet(natures);

				// only apply new nature, if the status is ok
				if (status.getCode() == IStatus.OK) {
				    description.setNatureIds(natures);
				    project.setDescription(description, monitor);
				} else {
					throw new ErhenaException(status.getMessage(), status.getException());
				}
			}
		} catch (CoreException ce) {

			throw new ErhenaException(ce);
		}

		return project;
	}

	@Override
	public IProject createRhenaProject(String component, String module, IProgressMonitor monitor) throws ErhenaException {

		return this.createRhenaProject(component, module, null, monitor);
	}

	/**
	 * Just do the basics: create a basic project.
	 *
	 * @param location
	 * @param projectName
	 * @throws CoreException
	 */
	@Override
	public IProject createProject(String projectName, URI projectLocation, IProgressMonitor monitor) throws ErhenaException {

		try {
			// it is acceptable to use the ResourcesPlugin class
			IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

			if (!newProject.exists()) {
				IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());

				desc.setLocationURI(projectLocation);

				newProject.create(desc, monitor);

				if (!newProject.isOpen()) {
					newProject.open(monitor);
				}
			}

			return newProject;
		} catch (CoreException coreException) {

			throw new ErhenaException(coreException);
		}
	}

	@Override
	public void deleteProject(IProject project, IProgressMonitor monitor) throws ErhenaException {

		try {
			project.delete(true, monitor);
		} catch (CoreException coreException) {

			throw new ErhenaException(coreException);
		}
	}

}
