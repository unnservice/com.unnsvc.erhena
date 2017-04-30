
package com.unnsvc.erhena.wizards.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.rhena.common.RhenaConstants;

@Component(name = "projectCreationService", service = IProjectCreationService.class)
public class ProjectCreationService extends AbstractProjectCreationService implements IProjectCreationService {

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
	public IProject createRhenaProject(String component, String module, IProgressMonitor monitor) throws ErhenaException {

		IProject project = createProject(component + "." + module, monitor);
		try {
			// create default descriptor
			IFile moduleDescriptor = project.getFile(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			if (!moduleDescriptor.exists()) {

				moduleDescriptor.create(getModuleTemplate(component, module), false, monitor);

				/**
				 * Configure nature
				 */
				IProjectDescription description = project.getDescription();
				description.setNatureIds(new String[] { JavaCore.NATURE_ID, RhenaNature.NATURE_ID });
				project.setDescription(description, monitor);
			}
		} catch (CoreException ce) {

			throw new ErhenaException(ce);
		}

		return project;
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
