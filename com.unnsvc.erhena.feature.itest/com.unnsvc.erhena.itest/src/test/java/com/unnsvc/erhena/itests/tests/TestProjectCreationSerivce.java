
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.core.builder.RhenaBuilder;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.erhena.itests.AbstractServiceTest;
import com.unnsvc.erhena.platform.service.IPlatformService;
import com.unnsvc.erhena.wizards.service.IProjectService;
import com.unnsvc.rhena.common.RhenaConstants;

public class TestProjectCreationSerivce extends AbstractServiceTest {

	@Inject
	protected IPlatformService platformService;
	@Inject
	protected IProjectService projectCreationService;

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
				projectCreationService.deleteProject(project, monitor);
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
			Assert.assertTrue(project.getFile("/" + RhenaConstants.MODULE_DESCRIPTOR_FILENAME).exists());
			Assert.assertTrue(project.hasNature(RhenaNature.NATURE_ID));
			assertBuilderPresent(project, RhenaBuilder.BUILDER_ID);
			;
			// check that it has builder configured?
		} finally {
			if (project != null) {
				projectCreationService.deleteProject(project, monitor);
				Assert.assertFalse(project.exists());
			}
		}
	}
}
