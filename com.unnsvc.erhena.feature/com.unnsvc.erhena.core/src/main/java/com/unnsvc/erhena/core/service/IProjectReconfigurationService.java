
package com.unnsvc.erhena.core.service;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IProjectReconfigurationService {

	public void reconfigureProjects(List<IProject> affectedProjects) throws RhenaException, CoreException;
}
