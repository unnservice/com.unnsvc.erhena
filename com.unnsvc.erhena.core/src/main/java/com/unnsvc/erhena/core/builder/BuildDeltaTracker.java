
package com.unnsvc.erhena.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class BuildDeltaTracker implements IResourceDeltaVisitor {

	private boolean moduleOrTarget = false;
	private IProject project;
	private List<IPath> resources;

	public BuildDeltaTracker(IProject project) {

		this.project = project;
		this.resources = new ArrayList<IPath>();
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		IResource res = delta.getResource();

		IPath relative = res.getFullPath().makeRelativeTo(project.getFullPath());

		if (relative.segmentCount() == 0) {
			// throws null pointer exception if segment count is 0 and we get
			// segment(0)
			return true;
		}

		if (relative.segment(0).equals("target") || relative.segment(0).equals("module.xml")) {
//			System.err.println("Don't enter " + relative);
			moduleOrTarget = true;
			return false;
		}

		if (relative.segmentCount() > 0) {
			resources.add(relative);
		}

		return true;
	}

	public boolean isModuleOrTarget() {

		return moduleOrTarget;
	}

	public List<IPath> getResources() {

		return resources;
	}
}
