
package com.unnsvc.erhena.platform.service;

import com.unnsvc.rhena.common.logging.ILogger;

public interface IRhenaService {

	public void newTransaction(IRhenaTransaction transaction);

	public ILogger getRhenaLogger();

	// public IRhenaModule newEntryPoint(String component, String module, String
	// version) throws RhenaException;

}
