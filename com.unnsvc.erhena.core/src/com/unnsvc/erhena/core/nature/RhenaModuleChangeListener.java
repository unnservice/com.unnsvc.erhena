
package com.unnsvc.erhena.core.nature;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class RhenaModuleChangeListener implements IResourceChangeListener {

	public RhenaModuleChangeListener() {

	}

	public void resourceChanged(IResourceChangeEvent event) {

		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
			return;
		}

		System.out.println("Something changed!");
		IResourceDelta delta = event.getDelta();

		IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {

			public boolean visit(IResourceDelta delta) {

				if (delta.getKind() != IResourceDelta.CHANGED)
					return true;
				if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
					return true;
				
				
				IResource resource = delta.getResource();
				// only interested in files with the "txt" extension
				if (resource.getType() == IResource.FILE && "txt".equalsIgnoreCase(resource.getFileExtension())) {
					// changed.add(resource);
				}
				return true;
			}
		};

		
//		event.getResource().getProject().build(kind, builderName, args, monitor);
		
		try {
			delta.accept(visitor);
		} catch (CoreException e) {
			e.printStackTrace();
			// @TODO open error dialog with syncExec or print to plugin
			// log file
		}
	}
}
