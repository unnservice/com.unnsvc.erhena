package com.unnsvc.erhena.core.registry;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

public interface IChangeRegistryService {

	public void addChanges(IProject project, Set<IPath> newChanges);

}
