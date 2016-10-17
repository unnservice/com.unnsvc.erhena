package com.unnsvc.erhena.platform;

import java.io.File;

import javax.annotation.PostConstruct;
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
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

@Singleton
@Creatable
public class RhenaPlatformService implements IRhenaPlatformService {


	@Inject
	private IEventBroker eventBroker;

	private IRhenaContext context;
	
	@PostConstruct
	public void postConstruct() throws Exception {

		RhenaConfiguration config = new RhenaConfiguration();
		configureLogging(config);

		context = new CachingResolutionContext(config);

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		context.getRepositories().add(new WorkspaceRepository(context, workspacePath));
	}
	
	private void configureLogging(RhenaConfiguration config) {

//		config.configureLoggingAppender(new ILoggingListener() {
//
//			@Override
//			public void append(LogEvent event) {
//
//				System.err.println("Posting event on topic");
//				eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, event);
//			}
//		});

		config.configureLoggingAppender(new AppenderBase<ILoggingEvent>() {

			@Override
			protected void append(ILoggingEvent event) {

				System.err.println("Calling appender...");
				eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, event);
			}
		});
	}

	@Override
	public IRhenaModule materialiseModel(String component, String module, String version) throws RhenaException {

		return context.materialiseModel(ModuleIdentifier.valueOf(component + ":" + module + ":" + version));
	}
}
