
package com.unnsvc.erhena.platform.service;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface IPlatformService {

	public void newTransaction(IRhenaTransaction transaction);

	public ILogger getRhenaLogger();

	public IDependencies collectDependencies(IRhenaModule module, EExecutionType type) throws RhenaException;

	// public IRhenaModule newEntryPoint(String component, String module, String
	// version) throws RhenaException;

}
