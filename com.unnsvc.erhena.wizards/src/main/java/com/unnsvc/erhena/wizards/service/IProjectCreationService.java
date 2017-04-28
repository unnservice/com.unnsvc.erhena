
package com.unnsvc.erhena.wizards.service;

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import com.unnsvc.erhena.common.exceptions.ErhenaException;

public interface IProjectCreationService {

	public IProject createProject(String projectName, URI projectLocation, IProgressMonitor monitor) throws ErhenaException;

	public IProject createProject(String projectName, IProgressMonitor monitor) throws ErhenaException;

	public void deleteProject(IProject project, IProgressMonitor monitor) throws ErhenaException;

}
