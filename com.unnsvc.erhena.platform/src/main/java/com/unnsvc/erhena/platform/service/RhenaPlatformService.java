
package com.unnsvc.erhena.platform.service;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.events.LogEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
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
	private IRhenaEngine engine;
	// module => projectName tracking
	private Map<URI, ModuleIdentifier> entryPoints;

	@Inject
	public RhenaPlatformService(IEventBroker eventBorker) {
		
		entryPoints = new HashMap<URI, ModuleIdentifier>();

		IRhenaConfiguration config = new RhenaConfiguration();
		config.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		config.addWorkspaceRepository(new WorkspaceRepository(config, new File("../../")));
		config.addWorkspaceRepository(new WorkspaceRepository(config, new File("../")));
		config.setLocalRepository(new LocalCacheRepository(config));
		config.setRunTest(true);
		config.setRunItest(true);
		config.setParallel(false);
		config.setPackageWorkspace(false);
		config.setInstallLocal(true);
		config.getListenerConfig().addListener(new IContextListener<LogEvent>() {

			@Override
			public Class<LogEvent> getType() {

				return LogEvent.class;
			}

			@Override
			public void onEvent(LogEvent evt) throws RhenaException {

				eventBorker.post(ErhenaConstants.TOPIC_LOGEVENT, evt);
			}
		});
		config.getListenerConfig().addListener(new IContextListener<ModuleAddRemoveEvent>() {

			@Override
			public Class<ModuleAddRemoveEvent> getType() {

				return ModuleAddRemoveEvent.class;
			}

			@Override
			public void onEvent(ModuleAddRemoveEvent evt) throws RhenaException {

				eventBorker.post(ErhenaConstants.TOPIC_MODULE_ADDREMOVE, evt);
			}
		});

		// context.addListener(new ProjectConfigurationHandler(this, context));

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		config.addWorkspaceRepository(new WorkspaceRepository(config, workspacePath));

		engine = new RhenaEngine(config);
	}

	@Override
	public IRhenaModule newWorkspaceEntryPoint(URI projectLocation) throws RhenaException {

		ModuleIdentifier identifier = entryPoints.get(projectLocation);
		if (identifier == null) {
			File moduleLocation = new File(projectLocation);
			identifier = Utils.readModuleIdentifier(moduleLocation);
		}

		IRhenaModule module = engine.materialiseModel(identifier);
		if (!entryPoints.containsKey(module.getIdentifier())) {
			entryPoints.put(projectLocation, module.getIdentifier());
		}

		return module;
	}

	public IRhenaExecution materialiseExecution(IRhenaModule module) throws RhenaException {

		return engine.materialiseExecution(module, EExecutionType.MAIN);
	}

	@Override
	public void destroyEntryPoint(ModuleIdentifier entryPoint) {

	}

	@Override
	public ModuleIdentifier getEntryPointIdentifier(String workspaceProjectName) {

		return entryPoints.get(workspaceProjectName);
	}

	public void dropFromCache(ModuleIdentifier identifier) {

		// context.dropFromCache(identifier);
	}
}
