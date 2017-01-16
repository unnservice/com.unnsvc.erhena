package com.unnsvc.erhena.platform.service;

import org.eclipse.core.resources.IProject;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IProjectService {

	public ModuleIdentifier manageProject(IProject resource) throws RhenaException;

}
