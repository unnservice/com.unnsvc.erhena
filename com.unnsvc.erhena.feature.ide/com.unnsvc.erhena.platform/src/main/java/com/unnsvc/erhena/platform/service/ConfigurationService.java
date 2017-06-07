
package com.unnsvc.erhena.platform.service;

import org.osgi.service.component.annotations.Component;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.userconf.UserConfigFactory;

@Component(service = IConfigurationService.class)
public class ConfigurationService implements IConfigurationService {

	private IRhenaConfiguration config;

	public ConfigurationService() throws RhenaException {

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
	public IRhenaConfiguration getConfig() throws ErhenaException {

		if (config == null) {
			try {
				config = UserConfigFactory.fromUserConfig();
				return config;
			} catch (RhenaException re) {
				/**
				 * @TODO this is redundant because erhena exception is a subtype
				 *       of rhenaException but eclipse PDE refuses to see the
				 *       RhenaException indirectly
				 */
				throw new ErhenaException(re);
			}
		} else {
			return config;
		}
	}
}
