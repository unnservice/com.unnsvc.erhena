
package com.unnsvc.erhena.platform.handlers;

public class ProjectConfigurationHandler {
// implements IContextListener<WorkspaceConfigurationEvent> {

//	private RhenaPlatformService platformService;
//	private IRhenaEngine context;
//
//	public ProjectConfigurationHandler(RhenaPlatformService platformService, IRhenaEngine context) {
//
//		this.platformService = platformService;
//		this.context = context;
//	}
//
//	/**
//	 * <pre>
//	 * 	get eclipse project from ModuleIdentifier
//	 *  configure its classpath using resources here
//	 * </pre>
//	 */
//	@Override
//	public void onEvent(WorkspaceConfigurationEvent event) throws RhenaException {
//
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IWorkspaceRoot root = workspace.getRoot();
//
//		String projectName = platformService.getProjectName(event.getModuleIdentifier());
//		IProject project = root.getProject(projectName);
//
//		System.err.println("Configuring: " + projectName);
//		System.err.println("ExecutionContext is: " + event.getExecutionContext());
//		IExecutionContext ec = event.getExecutionContext();
//
//		try {
//			onEvent(JavaCore.create(project), project, project.getDescription(), ec);
//		} catch (CoreException ce) {
//			throw new RhenaException(ce.getMessage(), ce);
//		}
//	}
//
//	private void onEvent(IJavaProject javaProject, IProject project, IProjectDescription description, IExecutionContext ec) throws CoreException {
//
//		// List<IClasspathEntry> entries = new
//		// ArrayList<IClasspathEntry>(Arrays.asList(project.getRawClasspath()));
//		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
//
//		for (EExecutionType eet : EExecutionType.values()) {
//			// There might not be any configuration for the model in the
//			// lifecycle, we ignore it for now
//			if (eet != EExecutionType.MODEL) {
//				/**
//				 * Why IFolder and IPackageFragmentRoot when we only need IPath
//				 * to create IClasspathEntry
//				 */
//				for (IResource resource : ec.getResources(eet)) {
//					IFolder source = project.getFolder(resource.getSourcePath());
//					IPackageFragmentRoot fragmentRoot = javaProject.getPackageFragmentRoot(source);
//
//					IFolder target = project.getFolder(resource.getTargetPath());
//
//					if (source.exists()) {
//
//						if (!target.exists()) {
//							RhenaUtils.createFolder(target);
//						}
//
//						IClasspathEntry entry = JavaCore.newSourceEntry(fragmentRoot.getPath(), null, target.getFullPath());
//						entries.add(entry);
//					}
//				}
//			}
//		}
//
//		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
//
//	}
//
//	@Override
//	public Class<WorkspaceConfigurationEvent> getType() {
//
//		return WorkspaceConfigurationEvent.class;
//	}
}
