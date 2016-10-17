
package com.unnsvc.erhena.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;

import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.rhena.common.RhenaConstants;

/**
 * @TODO generate lifecycle project too?
 * @author noname
 *
 */
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

	public static IProject createProject(String componentName, String projectName, URI location, IProgressMonitor monitor) throws CoreException {

		// URIUtil.toPath(location).

		// create
		// IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		// IProject project = root.getProject(componentName + "." +
		// projectName);
		//
		// if (!project.exists()) {
		// IProjectDescription desc =
		// ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
		// if
		// (!ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location))
		// {
		// desc.setLocationURI(location);
		// }
		// project.create(monitor);
		// if (!project.isOpen()) {
		// project.open(monitor);
		// }
		// }

		IProject project = createBaseProject(componentName + "." + projectName, location, monitor);

		// add nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID, RhenaNature.NATURE_ID });
		project.setDescription(description, monitor);

		IJavaProject javaProject = JavaCore.create(project);

		// out
		IFolder targetFolder = project.getFolder("target");
		targetFolder.create(false, true, monitor);
		IFolder outFolder = project.getFolder("target/eclipse");
		outFolder.create(false, true, monitor);
		javaProject.setOutputLocation(outFolder.getFullPath(), null);

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
		javaProject.setRawClasspath(new IClasspathEntry[] { jreEntry }, monitor);

		// create source folders
		String[] srcPaths = { "src/main/java", "src/main/resources", "src/test/java" };
		String[] outPath = { "target/eclipse" };
		// addToProjectStructure(project, paths);

		addToProjectStructure(project, srcPaths);
		addToProjectStructure(project, outPath);

		for (String path : srcPaths) {

			IFolder sourceFolder = project.getFolder(path);

			IPackageFragmentRoot fragmentRoot = javaProject.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(fragmentRoot.getPath());
			javaProject.setRawClasspath(newEntries, monitor);
		}

		// create default descriptor
		IFile moduleDescriptor = project.getFile(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
		if (!moduleDescriptor.exists()) {
			moduleDescriptor.create(getModuleTemplate(componentName, projectName), false, monitor);
		}

		return project;
	}

	private static InputStream getModuleTemplate(String componentName, String projectName) throws CoreException {

		URL moduleTemplate = Activator.class.getResource("/templates/module.xml.tpl");
		try (InputStream is = moduleTemplate.openStream()) {

			StringBuilder sb = new StringBuilder();
			int buff = -1;
			while ((buff = is.read()) != -1) {
				sb.append((char) buff);
			}
			String template = sb.toString().replaceAll(Pattern.quote("##COMPONENTID##"), componentName);
			ByteArrayInputStream bais = new ByteArrayInputStream(template.getBytes());
			return bais;
		} catch (IOException ioe) {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, ioe.getMessage(), ioe));
		}
	}

	/**
	 * Just do the basics: create a basic project.
	 *
	 * @param location
	 * @param projectName
	 * @throws CoreException
	 */
	private static IProject createBaseProject(String projectName, URI location, IProgressMonitor monitor) {

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

				newProject.create(desc, monitor);
				if (!newProject.isOpen()) {
					newProject.open(monitor);
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
}
