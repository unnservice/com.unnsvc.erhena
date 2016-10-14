
package com.unnsvc.erhena.wizards;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;

import com.unnsvc.erhena.core.nature.RhenaNature;

public class RhenaModuleProjectSupport {

	/**
	 * For this marvelous project we need to: - create the default Eclipse
	 * project - add the custom project nature - create the folder structure
	 *
	 * @param projectName
	 * @param location
	 * @param natureId
	 * @return
	 * @throws CoreException
	 */

	public static IProject createProject(String projectName, URI location) throws CoreException {

		// create
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		project.create(null);
		project.open(null);

		// add nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID, RhenaNature.NATURE_ID });
		project.setDescription(description, null);

		IJavaProject javaProject = JavaCore.create(project);

		// out
		IFolder binFolder = project.getFolder("target");
		binFolder.create(false, true, null);
		javaProject.setOutputLocation(binFolder.getFullPath(), null);

		// add jre?
		// List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		// LibraryLocation[] locations =
		// JavaRuntime.getLibraryLocations(vmInstall);
		// for (LibraryLocation element : locations) {
		// entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(),
		// null, null));
		// }
		// javaProject.setRawClasspath(entries.toArray(new
		// IClasspathEntry[entries.size()]), null);

		IPath containerPath = new Path(JavaRuntime.JRE_CONTAINER);
		IPath vmPath = containerPath.append(vmInstall.getVMInstallType().getId()).append(vmInstall.getName());
		IClasspathEntry jreEntry = JavaCore.newContainerEntry(vmPath);
		javaProject.setRawClasspath(new IClasspathEntry[] { jreEntry }, null);

		// create source folders
		String[] paths = { "src/main/java", "src/test/java" };
		// addToProjectStructure(project, paths);

		addToProjectStructure(project, paths);
		for (String path : paths) {

			IFolder sourceFolder = project.getFolder(path);

			IPackageFragmentRoot fragmentRoot = javaProject.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(fragmentRoot.getPath());
			javaProject.setRawClasspath(newEntries, null);
		}

		return project;
	}

	public static IProject createProject2(String projectName, URI location) {

		IProject project = createBaseProject(projectName, location);
		try {

			IProjectDescription desc = project.getDescription();

			// addNature(project, JavaCore.NATURE_ID);
			// IJavaProject javaProject = JavaCore.create(project);

			// addNature(project, RhenaNature.NATURE_ID);

			String[] natures = desc.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[newNatures.length - 1] = RhenaNature.NATURE_ID;

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IStatus status = workspace.validateNatureSet(newNatures);

			// only apply new nature, if the status is ok
			if (status.getCode() == IStatus.OK) {
				desc.setNatureIds(newNatures);
				project.setDescription(desc, null);
			}

			IJavaProject javaProject = JavaCore.create(project);

			// out
			IFolder binFolder = project.getFolder("target");
			binFolder.create(false, true, null);
			javaProject.setOutputLocation(binFolder.getFullPath(), null);

			String[] paths = { "src/main/java", "src/test/java" }; //$NON-NLS-1$ //$NON-NLS-2$
			addToProjectStructure(project, paths);

		} catch (CoreException e) {
			e.printStackTrace();
			project = null;
		}

		return project;
	}

	/**
	 * Just do the basics: create a basic project.
	 *
	 * @param location
	 * @param projectName
	 * @throws CoreException
	 */
	private static IProject createBaseProject(String projectName, URI location) {

		// it is acceptable to use the ResourcesPlugin class
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!newProject.exists()) {
			URI projectLocation = location;
			IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}

			desc.setLocationURI(projectLocation);
			try {

				newProject.create(desc, null);
				if (!newProject.isOpen()) {
					newProject.open(null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return newProject;
	}

	private static void createFolder(IFolder folder) throws CoreException {

		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}

	/**
	 * Create a folder structure with a parent root, overlay, and a few child
	 * folders.
	 *
	 * @param newProject
	 * @param paths
	 * @throws CoreException
	 */
	private static void addToProjectStructure(IProject newProject, String[] paths) throws CoreException {

		for (String path : paths) {
			IFolder etcFolders = newProject.getFolder(path);
			createFolder(etcFolders);
		}
	}

	private static void addNature(IProject project, String natureId) throws CoreException {

		if (!project.hasNature(natureId)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = natureId;
			description.setNatureIds(newNatures);

			IProgressMonitor monitor = null;
			project.setDescription(description, monitor);
		}
	}

}
