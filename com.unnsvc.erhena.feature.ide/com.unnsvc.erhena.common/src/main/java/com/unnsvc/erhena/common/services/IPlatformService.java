
package com.unnsvc.erhena.common.services;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;

public interface IPlatformService {

	public IRhenaConfiguration getConfig();

	public IRhenaEngine getEngine();

	public IRhenaContext getContext();

}
