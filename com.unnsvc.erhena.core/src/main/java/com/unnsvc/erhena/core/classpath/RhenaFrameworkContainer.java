
package com.unnsvc.erhena.core.classpath;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class RhenaFrameworkContainer implements IClasspathContainer {

	private IPath path;

	public RhenaFrameworkContainer(IPath path) {

		this.path = path;
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {

		try {
			URL coreLib = FileLocator.resolve(new URL("platform:/plugin/com.unnsvc.erhena.platform/embedded/com.unnsvc.rhena.core-0.0.1-SNAPSHOT.jar"));
			URL frameworkLib = FileLocator.resolve(new URL("platform:/plugin/com.unnsvc.erhena.platform/embedded/com.unnsvc.rhena.lifecycle-0.0.1-SNAPSHOT.jar"));

			return new IClasspathEntry[] { 
					JavaCore.newLibraryEntry(new Path(frameworkLib.getFile()), null, null),
					JavaCore.newLibraryEntry(new Path(coreLib.getFile()), null, null) 
			};
		} catch (IOException ioe) {
			/**
			 * @TODO pass the entries into the container instead of creating
			 *       them here, then we don't need to catch this exception
			 */
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getDescription() {

		return "eRhena Framework";
	}

	@Override
	public int getKind() {

		return IClasspathContainer.K_APPLICATION;
	}

	@Override
	public IPath getPath() {

		return path;
	}

}
