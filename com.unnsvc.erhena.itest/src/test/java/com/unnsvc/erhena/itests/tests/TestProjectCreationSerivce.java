
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.erhena.itests.AbstractServiceTest;
import com.unnsvc.erhena.platform.service.IPlatformService;
import com.unnsvc.erhena.wizards.service.IProjectCreationService;

public class TestProjectCreationSerivce extends AbstractServiceTest {

	@Inject
	private IPlatformService platformService;
	@Inject
	private IProjectCreationService projectCreationService;

	@Test
	public void testProjectCreation() throws Exception {

		Assert.assertNotNull(platformService);
		Assert.assertNotNull(projectCreationService);

		IProject project = null;
		try {
			project = projectCreationService.createProject("testProject", monitor);
			Assert.assertTrue(project.exists());
		} finally {
			if (project != null) {
				projectCreationService.deleteProject(project, new NullProgressMonitor());
				Assert.assertFalse(project.exists());
			}
		}
	}
	
	@Test
	public void createRhenaProject() throws Exception {
		
		IProject project = null;
		try {
			project = projectCreationService.createRhenaProject("com.test", "project", monitor);
			Assert.assertTrue(project.exists());
			Assert.assertTrue(project.getFile("/module.xml").exists());
			Assert.assertTrue(project.hasNature(RhenaNature.NATURE_ID));
			// check that it has builder configured?
		} finally {
			if(project != null) {
				projectCreationService.deleteProject(project, monitor);
				Assert.assertFalse(project.exists());
			}
		}
	}
}
