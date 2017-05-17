
package com.unnsvc.erhena.core.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.osgi.service.component.annotations.Component;

@Component(service = IChangeRegistryService.class)
public class ChangeRegistryService implements IChangeRegistryService {

	private Map<IProject, Set<IPath>> changes;

	public ChangeRegistryService() {

		this.changes = new HashMap<IProject, Set<IPath>>();
	}

	@Override
	public synchronized void addChanges(IProject project, Set<IPath> newChanges) {

		Set<IPath> change = changes.get(project);
		if (change == null) {
			change = new HashSet<IPath>();
		}
		change.addAll(newChanges);
	}
}
