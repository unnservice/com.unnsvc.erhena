
package com.unnsvc.erhena.platform.service;

import org.eclipse.core.resources.IProject;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRhenaService {

	// public IRhenaModule newEntryPoint(String component, String module, String
	// version) throws RhenaException;

	/**
	 * This method will release the resources associated with the model, for
	 * when a project is deleted or somehow removed from context
	 * 
	 * @param entryPoint
	 */
	public void destroyEntryPoint(ModuleIdentifier entryPoint) throws RhenaException;

	public void buildProject(IProject project);

}
