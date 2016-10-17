package com.unnsvc.erhena.platform.service;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaPlatformService {

	public IRhenaModule materialiseModel(String component, String module, String version) throws RhenaException;

}
