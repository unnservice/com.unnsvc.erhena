
package com.unnsvc.erhena.platform.service;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

@Singleton
@Creatable
public class ProjectService implements IProjectService {

	private Map<URI, ModuleIdentifier> managedProjects;
	private RhenaService rhena;

	@Inject
	public ProjectService(RhenaService rhena) {

		this.rhena = rhena;
		managedProjects = new HashMap<URI, ModuleIdentifier>();
	}

	public ModuleIdentifier manageProject(URI projectLocation) throws RhenaException {

		ModuleIdentifier identifier = managedProjects.get(projectLocation);

		if (identifier == null) {
			File moduleLocation = new File(projectLocation);
			identifier = Utils.readModuleIdentifier(moduleLocation);
			managedProjects.put(projectLocation, identifier);
		}
		return identifier;
	}

	// public IRhenaModule newWorkspaceEntryPoint(URI projectLocation) throws
	// RhenaException {
	//
	// ModuleIdentifier identifier = entryPoints.get(projectLocation);
	// if (identifier == null) {
	// File moduleLocation = new File(projectLocation);
	// identifier = Utils.readModuleIdentifier(moduleLocation);
	// }
	//
	// IRhenaModule module = rhena.getEngine().materialiseModel(identifier);
	// if (!entryPoints.containsKey(module.getIdentifier())) {
	// entryPoints.put(projectLocation, module.getIdentifier());
	// }
	//
	// return module;
	// }
	//
	// public ModuleIdentifier getEntryPointIdentifier(String
	// workspaceProjectName) {
	//
	// return entryPoints.get(workspaceProjectName);
	// }
}
