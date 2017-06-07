
package com.unnsvc.erhena.common.services;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;

/**
 * This service will hold the rhena settings
 * 
 * @author noname
 *
 */
public interface IConfigurationService {

	public IRhenaConfiguration getConfig() throws ErhenaException;

	public void persistRepositories() throws ErhenaException;

}
