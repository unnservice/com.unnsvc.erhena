
package com.unnsvc.erhena.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class RhenaFrameworkClasspathContainer extends ClasspathContainerInitializer {

	public static final String CONTAINER_ID = "com.unnsvc.erhena.core.frameworkClasspathContainer";

	public RhenaFrameworkClasspathContainer() {

	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {

		RhenaFrameworkContainer frameworkContainer = new RhenaFrameworkContainer(containerPath);
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { frameworkContainer }, null);
	}

}
