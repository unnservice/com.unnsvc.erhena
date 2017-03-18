
package com.unnsvc.erhena.itests.tests;

import java.net.URI;

import javax.inject.Inject;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.common.IRhenaProject;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.erhena.wizards.service.ProjectCreationService;

public class TestCreateProject extends AbstractTest {

	@Inject
	private ProjectCreationService projectCreationService;

	@Test
	public void testCreateProject() throws Exception {

		Assert.assertNotNull(projectCreationService);

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		Assert.assertNotNull(ws);

		String componentName = "com.test";
		String projectName = "project2";
		URI projectLocation = ws.getRoot().getLocation().append(componentName +"." + projectName).toFile().toURI();
		IRhenaProject project = projectCreationService.createProject(componentName, projectName, projectLocation, new NullProgressMonitor());

		Assert.assertNotNull(project.getProject().getNature(RhenaNature.NATURE_ID));
		Assert.assertEquals(projectLocation, project.getProject().getLocationURI());
		
		projectCreationService.deleteProject(project, new NullProgressMonitor());
	}

}
