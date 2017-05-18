
package com.unnsvc.erhena.core.classpath;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;

public class RhenaClasspathContainer implements IClasspathContainer {

	private String containerName;
	private IPath path;
	private List<IClasspathEntry> entries;

	public RhenaClasspathContainer(String containerName, IPath path, List<IClasspathEntry> classpathEntries) {

		this.containerName = containerName;
		this.path = path;
		this.entries = classpathEntries;
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {

		return entries.toArray(new IClasspathEntry[entries.size()]);
	}

	@Override
	public String getDescription() {

		return containerName;
	}

	@Override
	public int getKind() {

		return IClasspathContainer.K_APPLICATION;
	}

	@Override
	public IPath getPath() {

		return path;
	}

	public void addEntry(IClasspathEntry libraryEntry) {

		entries.add(libraryEntry);
	}

}
