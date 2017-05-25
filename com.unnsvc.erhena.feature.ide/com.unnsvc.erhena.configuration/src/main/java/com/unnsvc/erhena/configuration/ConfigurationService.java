
package com.unnsvc.erhena.configuration;

import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.config.RhenaConfiguration;

@Component(service = IConfigurationService.class)
public class ConfigurationService implements IConfigurationService {

	private IRhenaConfiguration config;

	public ConfigurationService() {

		config = new RhenaConfiguration();
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}
}
