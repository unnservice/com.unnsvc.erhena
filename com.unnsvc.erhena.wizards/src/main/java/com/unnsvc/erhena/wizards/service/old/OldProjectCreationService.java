
package com.unnsvc.erhena.wizards.service.old;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;

import com.unnsvc.erhena.common.ErhenaUtils;
import com.unnsvc.erhena.common.IRhenaProject;
import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.core.nature.RhenaNature;
import com.unnsvc.erhena.core.nature.RhenaProject;
import com.unnsvc.erhena.platform.service.OldPlatformService;
import com.unnsvc.erhena.wizards.Activator;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.RhenaConstants;

/**
 * @TODO generate lifecycle project too?
 * @author noname
 *
 */
@Creatable
@Singleton
public class OldProjectCreationService {

	@Inject
	private OldPlatformService platformService;

	public OldProjectCreationService() {

	}

	/**
	 * For this marvelous project we need to: - create the default Eclipse
	 * project - add the custom project nature - create the folder structure
	 *
	 * @param projectName
	 * @param location
	 * @param natureId
	 * @return
	 * @throws CoreException
	 * @throws ErhenaException
	 */

	public IRhenaProject createProject(String componentName, String projectName, URI projectLocation, IProgressMonitor monitor) throws CoreException {

		try {
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

			IProject project = createBaseProject(componentName + "." + projectName, projectLocation, monitor);

			// create default descriptor
			IFile moduleDescriptor = project.getFile(RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			if (!moduleDescriptor.exists()) {
				moduleDescriptor.create(getModuleTemplate(componentName, projectName), false, monitor);
			}

			// add nature
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID, RhenaNature.NATURE_ID });
			project.setDescription(description, monitor);

			/**
			 * Now we have a project in the workspace that is closed and has
			 * module.xml and erhena nature, perform rhena build on it
			 */

			IRhenaEngine engine = platformService.locatePlatform();

			System.err.println("Building created project with monitor " + monitor.hashCode());
			project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);

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

			return new RhenaProject(javaProject);
		} catch (ErhenaException ex) {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex));
		}
	}

	private InputStream getModuleTemplate(String componentName, String projectName) throws CoreException {

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
	private IProject createBaseProject(String projectName, URI projectLocation, IProgressMonitor monitor) throws CoreException {

		// it is acceptable to use the ResourcesPlugin class
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

		if (!newProject.exists()) {
			IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());

			desc.setLocationURI(projectLocation);

			newProject.create(desc, monitor);

			if (!newProject.isOpen()) {
				newProject.open(monitor);
			}
		}

		return newProject;
	}

	/**
	 * Create a folder structure with a parent root, overlay, and a few child
	 * folders.
	 *
	 * @param newProject
	 * @param paths
	 * @throws CoreException
	 */
	private void addToProjectStructure(IProject newProject, String[] paths) throws CoreException {

		for (String path : paths) {
			IFolder etcFolders = newProject.getFolder(path);
			ErhenaUtils.createFolder(etcFolders);
		}
	}

	public void deleteProject(IRhenaProject project, IProgressMonitor monitor) throws CoreException {

		project.getProject().delete(false, monitor);
	}
}
