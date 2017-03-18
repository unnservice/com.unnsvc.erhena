
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.platform.service.PlatformService;

public class TestCreateProject extends AbstractTest {

	@Inject
	private PlatformService platformService;

	@Test
	public void someTest() throws Exception {

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		Assert.assertNotNull(ws);


		Assert.assertNotNull(platformService);
	}

}
