
package com.unnsvc.erhena.core.nature;

import javax.inject.Inject;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.framework.BundleContext;

import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.core.builder.RhenaBuilder;
import com.unnsvc.erhena.platform.service.RhenaPlatformService;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class RhenaNature implements IProjectNature {

	public static final String NATURE_ID = "com.unnsvc.erhena.core.nature";
	private IProject project;

	@Inject
	private IEventBroker eventBroker;

	@Inject
	private IEclipseContext eclipseContext;

	@Inject
	private RhenaPlatformService rhenaPlatformService;

	// @Inject
	// private RhenaPlatformService rhenaService;

	public RhenaNature() {

		BundleContext bundleContext = Activator.getContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
		// IRhenaPlatformService instance =
		// ContextInjectionFactory.make(IRhenaPlatformService.class,
		// eclipseContext);
		System.err.println("Created: " + eclipseContext + " and injected? " + eventBroker + " platform service " + rhenaPlatformService);
	}

	@Override
	public void configure() throws CoreException {

		// IEclipseContext rootContext =
		// EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(RhenaService.class).getBundleContext());
		// IRhenaService service =
		// ContextInjectionFactory.make(RhenaService.class, rootContext);

		// System.err.println("Configuring, rhena service? " + rhenaService);
		//
		WorkspaceJob wj = new WorkspaceJob("Test") {

			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

				try {
					IRhenaModule module = rhenaPlatformService.materialiseModel("com.test", "com.test2", "0.0.1");

				} catch (RhenaException re) {
					throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, re.getMessage(), re));
				}
				return Status.OK_STATUS;
			}
		};
		wj.schedule();

		IProjectDescription desc = project.getDescription();
		ICommand[] commands = desc.getBuildSpec();

		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(RhenaBuilder.BUILDER_ID)) {
				return;
			}
		}

		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		ICommand command = desc.newCommand();
		command.setBuilderName(RhenaBuilder.BUILDER_ID);
		newCommands[newCommands.length - 1] = command;
		desc.setBuildSpec(newCommands);
		project.setDescription(desc, null);
	}

	@Override
	public void deconfigure() throws CoreException {

		System.err.println("Deconfiguring");

		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(RhenaBuilder.BUILDER_ID)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
				description.setBuildSpec(newCommands);
				project.setDescription(description, null);
				return;
			}
		}
	}

	@Override
	public IProject getProject() {

		return project;
	}

	@Override
	public void setProject(IProject project) {

		this.project = project;
	}

}
