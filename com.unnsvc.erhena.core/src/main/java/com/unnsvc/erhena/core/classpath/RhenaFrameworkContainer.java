
package com.unnsvc.erhena.core.classpath;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

import com.unnsvc.erhena.common.ErhenaUtils;

public class RhenaFrameworkContainer implements IClasspathContainer {

	private IPath path;

	public RhenaFrameworkContainer(IPath path) {

		this.path = path;
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {

		try {
			URL commonLib = ErhenaUtils.locateClasspathUrl("com.unnsvc.rhena.common");
			URL coreLib = ErhenaUtils.locateClasspathUrl("com.unnsvc.rhena.core");
			URL lifecycleLib = ErhenaUtils.locateClasspathUrl("com.unnsvc.rhena.lifecycle");
			URL agentLib = ErhenaUtils.locateClasspathUrl("com.unnsvc.rhena.agent");
			
			return new IClasspathEntry[] { 
					JavaCore.newLibraryEntry(new Path(commonLib.getFile()), null, null),
					JavaCore.newLibraryEntry(new Path(coreLib.getFile()), null, null),
					JavaCore.newLibraryEntry(new Path(lifecycleLib.getFile()), null, null),
					JavaCore.newLibraryEntry(new Path(agentLib.getFile()), null, null)
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
