
package com.unnsvc.erhena.core.nature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

import com.unnsvc.erhena.platform.service.IRhenaPlatformService;

public class RhenaModuleChangeListener implements IResourceChangeListener {

	public RhenaModuleChangeListener(IRhenaPlatformService platformService) {
	}

	public void resourceChanged(IResourceChangeEvent event) {

//		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
//			return;
//		}

		// if(event.getType() != IResourceChangeEvent.PRE_BUILD) {
		// return;
		// }
		


		

		IResourceDelta delta = event.getDelta();

		Map<IProject, Set<IResource>> resources = new HashMap<IProject, Set<IResource>>();

		IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {

			public boolean visit(IResourceDelta delta) {

				// if (delta.getKind() != IResourceDelta.CHANGED)
				// return true;
				// if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
				// return true;

				/**
				 * @TODO fix this mess at some point
				 */
				IResource resource = delta.getResource();
				IProject project = resource.getProject();
				try {

					if (project != null) {
						if (project.exists()) {
							if (project.isOpen()) {
								if (project.hasNature(RhenaNature.NATURE_ID)) {
									if (!resources.containsKey(project)) {
										resources.put(project, new HashSet<IResource>());
									}

									/**
									 * @TODO Figure it out dynamically
									 */
									IFolder folder = project.getFolder("target");
									IPath path = folder.getProjectRelativePath();

									// Don't enter target
									if (resource.getProjectRelativePath().equals(path)) {

										return false;
									} else {

										// @TODO we should only care about files
										// and not directories but idk how to
										// check if a path is a directory in
										// eclipse pde
										resources.get(project).add(resource);
									}

								}
							}
						}
					}

				} catch (CoreException ce) {
					// do some sort of logging?
					ce.printStackTrace();
				}

				// only interested in files with the "txt" extension
				// if (resource.getType() == IResource.FILE &&
				// "txt".equalsIgnoreCase(resource.getFileExtension())) {
				// resources.add(resource);
				// }
				return true;
			}
		};

		// event.getResource().getProject().build(kind, builderName, args,
		// monitor);

		try {
			delta.accept(visitor);
		} catch (CoreException e) {
			e.printStackTrace();
			// @TODO open error dialog with syncExec or print to plugin
			// log file
		}

		// @TODO do some black magic to determine which projects need building,
		// then...:

		for (IProject project : resources.keySet()) {

			Map<String, String> buildmap = new HashMap<String, String>();

			WorkspaceJob wj = new WorkspaceJob("Building " + project.getName()) {

				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

					try {
						build(project, buildmap);
					} catch (Exception re) {
						re.printStackTrace();
						// @TODO what to throw from here?
						return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}
			};
			wj.schedule();
		}

	}

	private void build(IProject project, Map<String, String> buildmap) throws Exception {

		// do something long running
		// ...

		// IJobManager jobManager = Job.getJobManager();

		// Wait for manual build to finish if running
		// jobManager.join(ResourcesPlugin.FAMILY_MANUAL_BUILD, new
		// NullProgressMonitor());

		// Wait for auto build to finish if running
		// jobManager.join(ResourcesPlugin.FAMILY_AUTO_BUILD, new
		// NullProgressMonitor());

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRunnable operation = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {

				try {
					project.build(IncrementalProjectBuilder.FULL_BUILD, "Rhena Builder", buildmap, new NullProgressMonitor());
				} catch (CoreException ce) {
					// @TODO some error?
					ce.printStackTrace();
				}
			}
		};

		try {

			workspace.run(operation, new NullProgressMonitor());
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
		// If you want to update the UI
		// sync.asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// // do something in the user interface
		// // e.g. set a text field
		// }
		// });

	}
}
