
package com.unnsvc.erhena.core.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.unnsvc.erhena.core.nature.RhenaNature;

public class DeltaCollector implements IResourceDeltaVisitor, Iterable<IPath> {

	private Map<IProject, List<IPath>> projectpaths;
	private List<IPath> paths;

	public DeltaCollector(IResourceChangeEvent event) throws CoreException {

		this.projectpaths = new HashMap<IProject, List<IPath>>();
		this.paths = new ArrayList<IPath>();
		if (event.getDelta() != null) {
			event.getDelta().accept(this);
		} else {
			onResource(event.getResource());
		}
	}

	private void onResource(IResource resource) throws CoreException {

		if (resource.getProject() != null) {
			IProject project = resource.getProject();
			if (project.hasNature(RhenaNature.NATURE_ID)) {

				IPath path = resource.getFullPath().makeRelativeTo(project.getFullPath());
				paths.add(path);

				List<IPath> projectpath = projectpaths.get(project);
				if (projectpath == null) {
					projectpath = new ArrayList<IPath>();
					projectpaths.put(project, projectpath);
				}
				projectpath.add(path);
			}
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

	public Map<IProject, List<IPath>> getProjectpaths() {

		return projectpaths;
	}

	public List<IPath> getPaths() {

		return paths;
	}
}
