
package com.unnsvc.erhena.itests.tests;

import javax.inject.Inject;

import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IPlatformService;
import com.unnsvc.erhena.common.services.IProjectService;
import com.unnsvc.erhena.core.builder.RhenaBuilder;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.erhena.itests.AbstractServiceTest;
import com.unnsvc.rhena.common.RhenaConstants;

@SuppressWarnings("restriction")
public class TestProjectCreationAndBuild extends AbstractServiceTest {

	@Inject
	protected IPlatformService platformService;
	@Inject
	protected IProjectService projectCreationService;

	private IProject project;

	@Test
	public void createRhenaProject() throws Exception {

		try {
			WorkspaceJob job = new WorkspaceJob("Create project") {

				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

					try {
						project = projectCreationService.createRhenaProject("com.test", "project", monitor);

						throw new UnsupportedOperationException("Not implemented");
//						IRhenaContext context = platformService.buildProject(project);
//						System.err.println("Context is " + context);

//						return Status.OK_STATUS;
					} catch (ErhenaException ee) {
						return new Status(Status.ERROR, Activator.PLUGIN_ID, ee.getMessage(), ee);
					}
				}
			};
			job.setRule(ResourcesPlugin.getWorkspace().getRoot());
			job.schedule();
			job.join();

			Assert.assertTrue(project.exists());
			Assert.assertTrue(project.getFile("/" + RhenaConstants.MODULE_DESCRIPTOR_FILENAME).exists());
			Assert.assertTrue(project.hasNature(RhenaNature.NATURE_ID));

			assertBuilderPresent(project, RhenaBuilder.BUILDER_ID);

			
//			/**
//			 * Run
//			 */
//			RhenaBuilderMonitorContext progress = new RhenaBuilderMonitorContext();
//			
//			//
//			project.build(IncrementalProjectBuilder.FULL_BUILD, progress);

			/**
			 * Assert that relevavnt projects have classpath
			 */
			
			
			WorkspaceJob job2 = new WorkspaceJob("1") {

				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

					System.err.println("Called here");
					return Status.OK_STATUS;
				}
			};
			job2.schedule();
			job2.join();

		} finally {
			if (project != null) {
				projectCreationService.deleteProject(project, monitor);
				Assert.assertFalse(project.exists());
			}
		}
	}
}
