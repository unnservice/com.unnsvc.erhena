
package com.unnsvc.erhena.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.rhena.common.RhenaConstants;

public class ModuleDeltaTracker implements IResourceDeltaVisitor {

	private List<IProject> projects;

	public ModuleDeltaTracker() {

		this.projects = new ArrayList<IProject>();
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		IResource res = delta.getResource();
		IProject project = res.getProject();
		if (res != null && project != null) {
			IPath relative = res.getFullPath().makeRelativeTo(project.getFullPath());
			if (relative.segmentCount() == 1 && res.getName().equals(RhenaConstants.MODULE_DESCRIPTOR_FILENAME)) {

				if (project.hasNature(RhenaNature.NATURE_ID)) {
					projects.add(project);
					return true;
				}
			} else if (relative.segmentCount() > 1) {
				return false;
			}
		}
		return true;
	}

	public List<IProject> getAffectedProjects() {

		return projects;
	}

}
