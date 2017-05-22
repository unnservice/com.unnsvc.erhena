
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.common.services.IWorkspacesService;
import com.unnsvc.erhena.itests.AbstractServiceTest;

public class TestWorkspacesService extends AbstractServiceTest {

	@Inject
	private IWorkspacesService workspacesService;

	@Test
	public void test() throws Exception {

		Assert.assertNotNull(workspacesService);
	}

}
