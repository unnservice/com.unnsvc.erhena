
package com.unnsvc.erhena.core.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class DeltaCollector implements IResourceDeltaVisitor, Iterable<IPath> {

	private Map<IProject, Set<IPath>> projectpaths;
	private List<IPath> paths;

	public DeltaCollector(IResourceChangeEvent event) throws CoreException {

		this.projectpaths = new HashMap<IProject, Set<IPath>>();
		this.paths = new ArrayList<IPath>();
		if (event.getDelta() != null) {
			event.getDelta().accept(this);
		} else {
			onResource(event.getResource());
		}
	}

	private void onResource(IResource resource) throws CoreException {

		if (resource.getProject() != null && resource.getProject().exists()) {
			IProject project = resource.getProject();
			// text throws
			// org.eclipse.core.internal.resources.ResourceException: Resource
			// '/com.test.project' does not exist.

			IPath path = resource.getFullPath().makeRelativeTo(project.getFullPath());
			paths.add(path);

			Set<IPath> projectpath = projectpaths.get(project);
			if (projectpath == null) {
				projectpath = new HashSet<IPath>();
				projectpaths.put(project, projectpath);
			}
			projectpath.add(path);
		}
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		onResource(delta.getResource());
		return true;
	}

	@Override
	public Iterator<IPath> iterator() {

		return paths.iterator();
	}

	public Map<IProject, Set<IPath>> getProjectpaths() {

		return projectpaths;
	}

	public List<IPath> getPaths() {

		return paths;
	}
}
