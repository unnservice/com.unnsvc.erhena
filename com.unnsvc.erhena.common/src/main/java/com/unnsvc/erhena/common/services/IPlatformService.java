package com.unnsvc.erhena.common.services;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.rhena.common.IRhenaEngine;

public interface IPlatformService {

	public IRhenaEngine locatePlatform() throws ErhenaException;

}
