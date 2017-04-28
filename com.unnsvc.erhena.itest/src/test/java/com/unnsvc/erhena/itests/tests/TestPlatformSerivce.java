
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.itests.AbstractServiceTest;
import com.unnsvc.erhena.platform.service.IPlatformService;
import com.unnsvc.erhena.wizards.service.IProjectCreationService;

public class TestPlatformSerivce extends AbstractServiceTest {

	@Inject
	private IPlatformService platformService;
	@Inject
	private IProjectCreationService projectCreationService;

	@Test
	public void test() throws Exception {

		Assert.assertNotNull(platformService);
		Assert.assertNotNull(projectCreationService);

		IProject project = null;
		try {
			project = projectCreationService.createProject("testProject", new NullProgressMonitor());
			Assert.assertTrue(project.exists());
		} finally {
			if (project != null) {
				projectCreationService.deleteProject(project, new NullProgressMonitor());
				Assert.assertFalse(project.exists());
			}
		}

	}
}
