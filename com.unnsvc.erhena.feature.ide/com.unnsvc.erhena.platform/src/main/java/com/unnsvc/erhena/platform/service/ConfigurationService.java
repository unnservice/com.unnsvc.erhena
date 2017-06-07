
package com.unnsvc.erhena.platform.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.config.userconf.UserConfigFactory;

@Component(service = IConfigurationService.class, scope = ServiceScope.SINGLETON)
public class ConfigurationService implements IConfigurationService {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaConfiguration config;

	public ConfigurationService() {

	}
	
	@Activate
	public void activate() throws ErhenaException {
		
		log.info("Activating configuration service, loading config");
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

		return config;
	}
}
