
package com.unnsvc.erhena.configuration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.RhenaConfiguration;
import com.unnsvc.rhena.config.settings.RhenaSettingsParser;
import com.unnsvc.rhena.config.settings.RhenaSettingsSerialiser;

@Component(service = IConfigurationService.class)
public class ConfigurationService implements IConfigurationService {

	private IRhenaConfiguration config;

	public ConfigurationService() throws RhenaException {

		config = new RhenaConfiguration();
		loadConfiguration();
	}

	private void loadConfiguration() throws RhenaException {

		File settingsFile = new File(System.getProperty("user.home") + File.separator + ".rhena/settings.xml");
		if (settingsFile.isFile()) {
			RhenaSettingsParser parser = new RhenaSettingsParser(config.getRepositoryConfiguration());
			parser.parseSettings(settingsFile);
		}
	}
	
	@Override
	public void persistConfiguration() throws ErhenaException {
		
		File settingsFile = new File(System.getProperty("user.home") + File.separator + ".rhena/settings.xml");
		settingsFile.getParentFile().mkdirs();
		
		RhenaSettingsSerialiser serialiser = new RhenaSettingsSerialiser(config);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(settingsFile))) {
			bos.write(serialiser.serialise().getBytes());
			bos.flush();
		} catch (IOException e) {
			
			throw new ErhenaException(e);
		}
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}
}
