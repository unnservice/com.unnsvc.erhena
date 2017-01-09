
package com.unnsvc.erhena.platform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.unnsvc.erhena.platform.service.IRhenaTransaction;
import com.unnsvc.erhena.platform.service.ProjectService;
import com.unnsvc.erhena.platform.service.RhenaService;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class PlatformResourceChangeListener implements IResourceChangeListener {

	private ProjectService projectService;
	private RhenaService platformService;

	public PlatformResourceChangeListener(RhenaService platformService, ProjectService projectService) {

		this.projectService = projectService;
		// System.err.println(getClass().getName() + "Created platform change
		// listener");
		this.platformService = platformService;
	}

	@Override
	public synchronized void resourceChanged(IResourceChangeEvent event) {

		// post resource change...

		// setListenerEnabled(false);

		// Modifications performed after disabling the listener, will be lost so
		// another build isn't scheduled...

		if (event.getDelta() != null) {

			IResourceDelta delta = event.getDelta();
			Map<IProject, Set<IResource>> resources = new HashMap<IProject, Set<IResource>>();
			// Set<IProject> affected = new HashSet<IProject>();

			IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {

				public boolean visit(IResourceDelta delta) {

					try {
						IResource resource = delta.getResource();
						if (resource.getType() == IResource.PROJECT) {
							IProject project = resource.getProject();
							if (project.hasNature("com.unnsvc.erhena.core.nature") && project.isOpen()) {
								resources.put(project, new HashSet<IResource>());
							}
							return false;
						}
						// else if (resource.getType() == IResource.FOLDER) {

						// }

						// IProject project = resource.getProject();
						//
						// if (project != null && project.isOpen()) {
						// if
						// (project.hasNature("com.unnsvc.erhena.core.nature"))
						// {
						// if (!resources.containsKey(project)) {
						// resources.put(project, new HashSet<IResource>());
						// }
						//// resources.get(project).add(resource);
						// }
						// return false;
						// }
					} catch (Exception ex) {

						ex.printStackTrace();
						return false;
					}

					return true;
				}
			};

			try {
				delta.accept(visitor);
			} catch (CoreException e) {

				e.printStackTrace();
			}

			if (!resources.isEmpty()) {
				WorkspaceJob wj = new WorkspaceJob("Building workspace") {

					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

						synchronized (PlatformResourceChangeListener.this) {
							
							System.err.println("Build transaction ==================================================");
							try {
								
								platformService.newTransaction(new IRhenaTransaction() {

									@Override
									public void execute(IRhenaEngine engine) throws CoreException {

										for (IProject affected : resources.keySet()) {

											// affected.build(IncrementalProjectBuilder.FULL_BUILD,
											// monitor);
											try {
												ModuleIdentifier affectedIdentifier = projectService.manageProject(affected);
												for (ModuleIdentifier root : platformService.getEngine().findRoots(affectedIdentifier, EExecutionType.TEST)) {

													System.err.println("Building root: " + root);
													IRhenaModule rootModule = engine.materialiseModel(root);
													IRhenaExecution exec = engine.materialiseExecution(rootModule, EExecutionType.TEST);
												}
											} catch (RhenaException re) {
												throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, re.getMessage(), re));
											}
										}
									}
								});

							} catch (Throwable t) {

								return new Status(IStatus.ERROR, Activator.PLUGIN_ID, t.getMessage(), t);
							} finally {

								// setListenerEnabled(true);
							}
						}

						return new Status(IStatus.OK, Activator.PLUGIN_ID, "Finished build workspace");
					}
				};

				wj.schedule();
			} else {

				// No workspace operation so we enable it immediately
//				setListenerEnabled(true);
			}
		}
	}

	private synchronized void setListenerEnabled(boolean enabled) {

		if (enabled) {

			ResourcesPlugin.getWorkspace().addResourceChangeListener(PlatformResourceChangeListener.this);
		} else {

			ResourcesPlugin.getWorkspace().removeResourceChangeListener(PlatformResourceChangeListener.this);
		}
	}
}

// IResourceRuleFactory ruleFactory =
// ResourcesPlugin.getWorkspace().getRuleFactory();

// wj.setRule(ResourcesPlugin.getWorkspace().getRoot());
// System.err.println("Changed: " +
// resource.getRawLocation());
//
// System.err.println(getClass().getName() + " DELTA
// Affected resource: " + resource + " resource project
// " + project);

//
// System.err.println(getClass().getName() + "
// POST_CHANGE
// event, building " + affected.getName());
//
// ModuleIdentifier identifier =
// projectService.getEntryPointIdentifier(affected.getName());
//
// platformService.dropFromCache(identifier);
//
// IRhenaModule module =
// projectService.newWorkspaceEntryPoint(affected.getProject().getLocationURI());
// IRhenaExecution execution =
// platformService.materialiseExecution(module);
//

// System.err.println("On resource change event");
// System.err.println("-------------- Change event");
// System.err.println(IResourceChangeEvent.POST_CHANGE ^ event.getType());
// System.err.println(IResourceChangeEvent.POST_BUILD ^ event.getType());
// System.err.println(IResourceChangeEvent.PRE_BUILD ^ event.getType());
// System.err.println(IResourceChangeEvent.PRE_DELETE ^ event.getType());
// System.err.println(IResourceChangeEvent.PRE_CLOSE ^ event.getType());
// System.err.println("-------------- /");