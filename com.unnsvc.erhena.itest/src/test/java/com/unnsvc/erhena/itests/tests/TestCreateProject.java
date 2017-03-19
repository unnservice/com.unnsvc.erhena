
package com.unnsvc.erhena.itests.tests;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.common.IRhenaProject;
import com.unnsvc.erhena.core.nature.RhenaNature;

public class TestCreateProject extends AbstractProjectCreationTest {

	@Test
	public void testCreateProject() throws Exception {

		Assert.assertNotNull(getProjectCreationService());

		String componentName = "com.test.component";
		String projectName = "project1";
		IRhenaProject project = createProject(componentName, projectName, getWorkspaceRoot().getLocation());

		Assert.assertNotNull(project.getProject().getNature(RhenaNature.NATURE_ID));
		Assert.assertEquals(getWorkspaceRoot().getLocation().append(componentName + "." + projectName).toFile().toURI().toString(), project.getProject().getLocationURI() + "/");

		getProjectCreationService().deleteProject(project, new NullProgressMonitor());
	}

}
