
package com.unnsvc.erhena.core.classpath;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;

public class RhenaClasspathContainerInitializer extends ClasspathContainerInitializer {

	public static final String CONTAINER_ID = "com.unnsvc.erhena.core.classpathContainer";
//	private List<IClasspathEntry> classpathEntries;

	public RhenaClasspathContainerInitializer() {

//		this.classpathEntries = new ArrayList<IClasspathEntry>();
	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {

//		RhenaClasspathContainer container = new RhenaClasspathContainer("eRhena Main", containerPath, classpathEntries);
//		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { container }, null);
//		System.err.println("INitializing classpath container");
	}
//
//	private RhenaClasspathContainer buildClasspathContainer(IPath containerPath, IJavaProject project) {
//
//		return new RhenaClasspathContainer("eRhena Main", containerPath, classpathEntries);
//	}

	@Override
	public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {

		return true;
	}

	@Override
	public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject project, IClasspathContainer containerSuggestion) throws CoreException {

		System.err.println("Requesting classpath container update");

		initialize(containerPath, project);

		// JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {
		// project }, new IClasspathContainer[] { containerSuggestion }, null);
	}
}
