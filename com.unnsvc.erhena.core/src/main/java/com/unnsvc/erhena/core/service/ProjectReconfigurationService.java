
package com.unnsvc.erhena.core.service;

import java.util.List;

import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.unnsvc.rhena.common.exceptions.RhenaException;

@Singleton
public class ProjectReconfigurationService implements IProjectReconfigurationService {

//	@Inject
//	private IPlatformService platformService;
//	@Inject
//	private IProjectService projectService;

	public ProjectReconfigurationService() {

	}

	@Override
	public void reconfigureProjects(List<IProject> affectedProjects) throws RhenaException, CoreException {

//		platformService.newTransaction(new IRhenaTransaction() {
//
//			@Override
//			public void execute(IRhenaEngine engine) throws Throwable {
//				// Perform rhena transaction here over all affected projects
//
//				System.err.println("Changed in :" + affectedProjects);
//
//				for (IProject project : affectedProjects) {
//					ModuleIdentifier id = projectService.manageProject(project);
//					onModuleChange(engine, project, id);
//				}
//			}
//		});
	}
//
//	private void onModuleChange(IRhenaEngine engine, IProject project, ModuleIdentifier identifier) throws CoreException, RhenaException {
//
//		IJavaProject javaProject = JavaCore.create(project);
//		IRhenaModule module = engine.materialiseModel(identifier);
//
//		System.err.println("Model is: " + module);
//		System.err.println("Execution is: " + engine.materialiseExecution(new Caller(module, EExecutionType.TEST)));
//		// assume it is always a workspace execution because the module.xml
//		// change detection occurs in a workspace project
//		WorkspaceExecution execution = (WorkspaceExecution) engine.materialiseExecution(new Caller(module, EExecutionType.TEST));
//
//		/**
//		 * If we've gotten this far and there are no exceptions, proceed with
//		 * reconfiguring the classpath
//		 * 
//		 * @TODO reconfigure classpath only when something changed in the
//		 *       classpath
//		 */
//
//		// Source paths
//		List<IClasspathEntry> sourcePaths = new ArrayList<IClasspathEntry>();
//		for (IResource resource : execution.getInputs()) {
//			System.err.println(javaProject.getProject().getName() + " Adding source folder: " + resource.getOriginalRelativeSourcePath());
//			IFolder sourceFolder = project.getFolder(resource.getOriginalRelativeSourcePath());
//			if (sourceFolder.exists()) {
//				// must be in a workspace job otherwise we get resource change
//				// modification error, so don't create it just set it when it
//				// exsts
//				// ErhenaUtils.createFolder(sourceFolder);
//				IPackageFragmentRoot fragmentRoot = javaProject.getPackageFragmentRoot(sourceFolder);
//				IClasspathEntry entry = JavaCore.newSourceEntry(fragmentRoot.getPath());
//				sourcePaths.add(entry);
//			}
//		}
//
//		// JVM entry
//		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
//		IPath jreContainerPath = new Path(JavaRuntime.JRE_CONTAINER);
//		IPath vmPath = jreContainerPath.append(vmInstall.getVMInstallType().getId()).append(vmInstall.getName());
//		IClasspathEntry jreEntry = JavaCore.newContainerEntry(vmPath);
//		sourcePaths.add(jreEntry);
//
//		// RhenaClasspathContainerInitializer initializer =
//		// (RhenaClasspathContainerInitializer)
//		// JavaCore.getClasspathContainerInitializer(containerPath.segment(0));
//		// initializer.addClasspathEntry(JavaCore.newLibraryEntry(new
//		// Path("/home/noname/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar"),
//		// null, null));
//
//		IPath containerPath = new Path(RhenaClasspathContainerInitializer.CONTAINER_ID);
//
//		IDependencies dependencies = platformService.collectDependencies(module, EExecutionType.TEST);
//
//		IPath mainPath = containerPath.append(project.getName() + "_main");
//		RhenaClasspathContainer mainTypeContainer = new RhenaClasspathContainer("eRhena Main", mainPath,
//				toList(mainPath, dependencies.getDependencies(EExecutionType.MAIN)));
//		JavaCore.setClasspathContainer(mainPath, new IJavaProject[] { javaProject }, new IClasspathContainer[] { mainTypeContainer }, null);
//
//		IPath testPath = containerPath.append(project.getName() + "_test");
//		RhenaClasspathContainer testTypeContainer = new RhenaClasspathContainer("eRhena Test", testPath,
//				toList(testPath, dependencies.getDependencies(EExecutionType.TEST)));
//		JavaCore.setClasspathContainer(testPath, new IJavaProject[] { javaProject }, new IClasspathContainer[] { testTypeContainer }, null);
//
//		sourcePaths.add(JavaCore.newContainerEntry(mainPath));
//		sourcePaths.add(JavaCore.newContainerEntry(testPath));
//
//		// rhena framework library
//		if (module.getModuleType().equals(ERhenaModuleType.FRAMEWORK)) {
//			IClasspathEntry frameworkContainer = JavaCore.newContainerEntry(new Path(RhenaFrameworkClasspathContainer.CONTAINER_ID));
//			sourcePaths.add(frameworkContainer);
//		}
//
//		WorkspaceJob job = new WorkspaceJob("Setting classpath") {
//
//			@Override
//			public IStatus runInWorkspace(IProgressMonitor monitor) {
//
//				try {
//					javaProject.setRawClasspath(sourcePaths.toArray(new IClasspathEntry[sourcePaths.size()]), new NullProgressMonitor());
//				} catch (JavaModelException e) {
//
//					e.printStackTrace();
//					return Status.CANCEL_STATUS;
//				}
//
//				return Status.OK_STATUS;
//			}
//		};
//		job.schedule();
//
//	}
//
//	private List<IClasspathEntry> toList(IPath containerPath, List<IRhenaExecution> list) {
//
//		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
//		for (IRhenaExecution exec : list) {
//
//			for (IArtifactDescriptor descriptor : exec.getArtifacts()) {
//				if (descriptor instanceof IExplodedArtifactDescriptor) {
//					IExplodedArtifactDescriptor exploded = (IExplodedArtifactDescriptor) descriptor;
//					IClasspathEntry entry = JavaCore.newLibraryEntry(new Path(exploded.getArtifactUrl().getPath()), new Path(exploded.getArtifactSourceUrl().getPath()), null);
//					entries.add(entry);
//				} else {
//					IClasspathEntry entry = JavaCore.newLibraryEntry(new Path(descriptor.getArtifactUrl().getPath()), null, null);
//					entries.add(entry);
//				}
//			}
//		}
//		System.err.println("Setting classpath entries for " + containerPath + " to: " + entries + " from " + list);
//		return entries;
//	}
}
