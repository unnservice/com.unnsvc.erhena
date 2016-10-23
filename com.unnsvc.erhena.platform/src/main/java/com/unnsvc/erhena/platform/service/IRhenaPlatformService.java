
package com.unnsvc.erhena.platform.service;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaPlatformService {

	// public IRhenaModule newEntryPoint(String component, String module, String
	// version) throws RhenaException;

	/**
	 * This method will release the resources associated with the model, for
	 * when a project is deleted or somehow removed from context
	 * 
	 * @param entryPoint
	 */

	public IRhenaModule newWorkspaceEntryPoint(String projectName) throws RhenaException;

	public void destroyEntryPoint(ModuleIdentifier entryPoint) throws RhenaException;

	/**
	 * @param moduleIdentifier
	 * @return null if no workspace project mapping exists
	 */
	public String getProjectName(ModuleIdentifier moduleIdentifier);

}
