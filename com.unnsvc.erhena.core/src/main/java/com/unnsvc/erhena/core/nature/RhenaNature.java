
package com.unnsvc.erhena.core.nature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;

import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.core.builder.RhenaBuilder;
import com.unnsvc.erhena.platform.service.IPlatformService;

public class RhenaNature implements IProjectNature {

	public static final String NATURE_ID = "com.unnsvc.erhena.core.nature";
	private IProject project;

	@Inject
	private IEventBroker eventBroker;

	@Inject
	private IEclipseContext eclipseContext;

	@Inject
	private IPlatformService rhenaPlatformService;

	public RhenaNature() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	public void configure() throws CoreException {

		IProjectDescription desc = project.getDescription();
		List<ICommand> commands = new ArrayList<ICommand>(Arrays.asList(desc.getBuildSpec()));

		if (commands.contains(RhenaBuilder.BUILDER_ID)) {
			return;
		} else {

			ICommand command = desc.newCommand();
			command.setBuilderName(RhenaBuilder.BUILDER_ID);
			commands.add(command);

			desc.setBuildSpec(commands.toArray(new ICommand[commands.size()]));
			project.setDescription(desc, null);
		}

		try {
			ProjectScope ps = new ProjectScope(getProject());
			IEclipsePreferences prefs = ps.getNode(Activator.PLUGIN_ID);
			prefs.put("key", "value");
			prefs.flush();
		} catch (BackingStoreException ioe) {

			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, ioe.getMessage(), ioe));
		}
	}

	@Override
	public void deconfigure() throws CoreException {

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
