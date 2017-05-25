
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.erhena.itests.AbstractServiceTest;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;

public class TestConfigurationService extends AbstractServiceTest {

	@Inject
	private IConfigurationService configService;

	@Test
	public void testConfiguration() {

		Assert.assertNotNull(configService);
		Assert.assertNotNull(configService.getConfig());
	}
}
