package com.unnsvc.erhena.common.services;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;

/**
 * This service will hold the rhena settings
 * @author noname
 *
 */
public interface IConfigurationService {

	public IRhenaConfiguration getConfig();

	public void persistConfiguration() throws ErhenaException;

	public void loadConfiguration() throws RhenaException;

}
