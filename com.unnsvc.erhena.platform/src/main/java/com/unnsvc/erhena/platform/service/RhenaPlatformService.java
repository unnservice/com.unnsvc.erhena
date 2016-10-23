
package com.unnsvc.erhena.platform.service;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.erhena.platform.handlers.ProjectConfigurationHandler;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.logging.LogEvent;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

/**
 * @TODO As the platform service handlest most eRhena oepartions, test it
 * 
 * @author noname
 *
 */
@Singleton
@Creatable
public class RhenaPlatformService implements IRhenaPlatformService {

	/**
	 * Single context throughout the application
	 */
	private IRhenaContext context;
	private Set<ModuleIdentifier> entryPoints;

	@Inject
	public RhenaPlatformService(IEventBroker eventBorker) {

		entryPoints = new HashSet<ModuleIdentifier>();

		RhenaConfiguration config = new RhenaConfiguration();
		context = new CachingResolutionContext(config);

		context.addListener(new IContextListener<LogEvent>() {

			@Override
			public Class<LogEvent> getType() {

				return LogEvent.class;
			}

			@Override
			public void onEvent(LogEvent evt) throws RhenaException {

				eventBorker.post(ErhenaConstants.TOPIC_LOGEVENT, evt);
			}
		});

		context.addListener(new IContextListener<ModuleAddRemoveEvent>() {

			@Override
			public Class<ModuleAddRemoveEvent> getType() {

				return ModuleAddRemoveEvent.class;
			}

			@Override
			public void onEvent(ModuleAddRemoveEvent evt) throws RhenaException {

				eventBorker.post(ErhenaConstants.TOPIC_MODULE_ADDREMOVE, evt);
			}
		});

		context.addListener(new ProjectConfigurationHandler(context));

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		context.getRepositories().add(new WorkspaceRepository(context, workspacePath));
	}

	@Override
	public IRhenaModule newWorkspaceEntryPoint(String projectName) throws RhenaException {

		IRhenaModule entryPoint = context.materialiseWorkspaceModel(projectName);
		if (!entryPoints.contains(entryPoint.getModuleIdentifier())) {
			entryPoints.add(entryPoint.getModuleIdentifier());
		}
		return entryPoint;
	}

	@Override
	public void destroyEntryPoint(ModuleIdentifier entryPoint) {

	}
}
