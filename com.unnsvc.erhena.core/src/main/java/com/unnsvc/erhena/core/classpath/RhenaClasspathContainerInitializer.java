
package com.unnsvc.erhena.core.classpath;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class RhenaClasspathContainerInitializer extends ClasspathContainerInitializer {

	public static final String CONTAINER_ID = "com.unnsvc.erhena.core.classpathContainer";
	private List<IClasspathEntry> classpathEntries;

	public RhenaClasspathContainerInitializer() {

		this.classpathEntries = new ArrayList<IClasspathEntry>();
	}

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		
		RhenaClasspathContainer container = new RhenaClasspathContainer("eRhena Main", containerPath, classpathEntries);
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { container }, null);
	}

	@Override
	public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {

		return false;
	}

	@Override
	public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject project, IClasspathContainer containerSuggestion) throws CoreException {

		// initialize(containerPath, project);

		// JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {
		// project }, new IClasspathContainer[] { containerSuggestion }, null);

	}

	public void addClasspathEntry(IClasspathEntry classpathEntry) {

		if(!classpathEntries.contains(classpathEntry)) {
			classpathEntries.add(classpathEntry);
		}
	}

}
