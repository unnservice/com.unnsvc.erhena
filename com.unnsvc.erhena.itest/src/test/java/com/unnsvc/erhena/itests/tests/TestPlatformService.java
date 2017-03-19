
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.platform.service.PlatformService;
import com.unnsvc.rhena.common.IRhenaEngine;

public class TestPlatformService extends AbstractTest {

	@Inject
	private PlatformService platformService;

	@Test
	public void testPlatformService() throws ErhenaException {
		
		Assert.assertNotNull(platformService);
		IRhenaEngine engine = platformService.locatePlatform();
		Assert.assertNotNull(engine);
	}
}
