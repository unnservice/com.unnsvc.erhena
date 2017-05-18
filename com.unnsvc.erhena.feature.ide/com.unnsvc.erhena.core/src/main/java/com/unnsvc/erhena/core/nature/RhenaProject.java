
package com.unnsvc.erhena.core.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import com.unnsvc.erhena.common.IRhenaProject;

/**
 * @TODO This class needs to eventually perform all classpath operations and have an update() method
 * @author noname
 *
 */
public class RhenaProject implements IRhenaProject {

	private IProject project;
	private IJavaProject javaProject;

	public RhenaProject(IJavaProject javaProject) {

		this.project = javaProject.getProject();
		this.javaProject = javaProject;
	}

	@Override
	public IProject getProject() {

		return project;
	}

	@Override
	public IJavaProject getJavaProject() {

		return javaProject;
	}
}
