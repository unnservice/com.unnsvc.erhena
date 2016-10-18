
package com.unnsvc.erhena.platform.service;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.logging.LogEvent;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

@Singleton
@Creatable
public class RhenaPlatformService implements IRhenaPlatformService {

	private IRhenaContext context;

	@Inject
	public RhenaPlatformService(IEventBroker eventBorker) {

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

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		context.getRepositories().add(new WorkspaceRepository(context, workspacePath));
	}

	@Override
	public IRhenaModule materialiseModel(String component, String module, String version) throws RhenaException {

		return context.materialiseModel(ModuleIdentifier.valueOf(component + ":" + module + ":" + version));
	}
}
