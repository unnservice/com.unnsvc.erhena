
package com.unnsvc.erhena.userconfig.services;

import java.io.File;

import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.RhenaConfiguration;
import com.unnsvc.rhena.config.userconf.UserConfigFactory;

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
				config = UserConfigFactory.fromUserConfig();
			} catch (RhenaException re) {
				/**
				 * @TODO this is redundant because erhena exception is a subtype
				 *       of rhenaException but eclipse PDE refuses to see the
				 *       RhenaException indirectly
				 */
				throw new ErhenaException(re);
			}
		} else {
			config = new RhenaConfiguration();
		}
	}

	@Override
	public void persistRepositories() throws ErhenaException {

		try {
			UserConfigFactory.serialiseRepositories(config.getRepositoryConfiguration());
		} catch (RhenaException re) {
			/**
			 * @TODO see above on redundant exception catching
			 */
			throw new ErhenaException(re);
		}
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}
}
