
package com.unnsvc.erhena.itests.tests;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.unnsvc.erhena.common.IRhenaProject;
import com.unnsvc.erhena.wizards.service.ProjectCreationService;

public abstract class AbstractProjectCreationTest extends AbstractTest {

	@Inject
	private ProjectCreationService projectCreationService;

	protected IWorkspaceRoot getWorkspaceRoot() {

		return ResourcesPlugin.getWorkspace().getRoot();
	}

	protected IRhenaProject createProject(String componentName, String projectName, IPath location) throws CoreException {

		URI projectLocation = location.append(componentName + "." + projectName).toFile().toURI();
		IRhenaProject project = projectCreationService.createProject(componentName, projectName, projectLocation, new NullProgressMonitor());
		return project;
	}

	public ProjectCreationService getProjectCreationService() {

		return projectCreationService;
	}
}
