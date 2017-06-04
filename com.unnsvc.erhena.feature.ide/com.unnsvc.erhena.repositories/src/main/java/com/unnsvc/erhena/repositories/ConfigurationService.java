
package com.unnsvc.erhena.repositories;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.RhenaConfiguration;
import com.unnsvc.rhena.config.settings.ConfigParser;
import com.unnsvc.rhena.config.settings.ConfigSerialiser;

@Component(service = IConfigurationService.class)
public class ConfigurationService implements IConfigurationService {

	private IRhenaConfiguration config;

	public ConfigurationService() throws RhenaException {

	}

	@Override
	public void loadConfiguration() throws ErhenaException {

		File settingsFile = new File(System.getProperty("user.home") + File.separator + ".rhena" + File.separator + "settings.xml");
		if (settingsFile.exists() && settingsFile.isFile()) {

			try {
				config = ConfigParser.parseConfig(settingsFile.toURI());
			} catch (RhenaException ioe) {

				config = new RhenaConfiguration();
				throw new ErhenaException(ioe);
			}
		} else {
			config = new RhenaConfiguration();
		}
	}

	@Override
	public void persistConfiguration() throws ErhenaException {

		File settingsFile = new File(System.getProperty("user.home") + File.separator + ".rhena" + File.separator + "settings.xml");
		settingsFile.getParentFile().mkdirs();

		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingsFile)))) {
			ConfigSerialiser.serialiseConfig(config, (indent, line) -> {
				writer.write(ConfigSerialiser.indents(indent) + line + System.getProperty("line.separator"));
			});
		} catch (IOException ioe) {
			throw new ErhenaException(ioe);
		}
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}
}
