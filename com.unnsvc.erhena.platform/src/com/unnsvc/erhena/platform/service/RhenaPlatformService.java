
package com.unnsvc.erhena.platform.service;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

@Singleton
@Creatable
public class RhenaPlatformService implements IRhenaPlatformService {

	@Inject
	private IEventBroker eventBroker;

	private IRhenaContext context;

	public RhenaPlatformService() {

		System.err.println("Constructing IRhenaPlatformService");

		RhenaConfiguration config = new RhenaConfiguration();
		System.err.println("Rhena config classloader: " + config.getClass().getClassLoader());

//		AppenderBase<ILoggingEvent> customAppender = new AppenderBase<ILoggingEvent>() {
//
//			@Override
//			protected void append(ILoggingEvent event) {
//
//				System.err.println("Calling appender...");
//				eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, event);
//			}
//		};
//
//		checkClassloader(customAppender);
//
//		configureLogging(config);

		context = new CachingResolutionContext(config);

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		context.getRepositories().add(new WorkspaceRepository(context, workspacePath));
	}

//	private void checkClassloader(AppenderBase<ILoggingEvent> appender) {
//
//		ClassLoader appenderClassloader = appender.getClass().getClassLoader();
//		ClassLoader thisClassloader = getClass().getClassLoader();
//
//		System.err.println("Appender classloader equals this classloader? " + (appenderClassloader == thisClassloader));
//		System.err.println("Appender classloader: " + appenderClassloader);
//		System.err.println("this classloader is: " + thisClassloader);
//	}

	public void testLog() {

		LoggerFactory.getLogger("com.unnsvc.rhena").info("TEST LOG");
		System.err.println("Test log with logger from classloader: " + LoggerFactory.getLogger("com.unnsvc.rhena"));
	}

	private void configureLogging(RhenaConfiguration config) {

		// config.configureLoggingAppender(new ILoggingListener() {
		//
		// @Override
		// public void append(LogEvent event) {
		//
		// System.err.println("Posting event on topic");
		// eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, event);
		// }
		// });

//		AppenderBase<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>() {
//
//			@Override
//			protected void append(ILoggingEvent event) {
//
//				System.err.println("Calling appender creaated in RhenaPlatformService configtureLogging");
//				eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, event);
//			}
//
//		};
//
//		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//		// rootLogger.detachAndStopAllAppenders();
//		rootLogger.setLevel(Level.INFO);
//
//		ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.unnsvc.rhena");
//		log.setLevel(Level.INFO);
//
//		LoggerContext c = rootLogger.getLoggerContext();
//
//		appender.setContext(c);
//
//		rootLogger.addAppender(appender);

//		config.configureLoggingAppender(new AppenderBase<ILoggingEvent>() {
//
//			@Override
//			protected void append(ILoggingEvent event) {
//
//				System.err.println("Calling appender...");
//				eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, event);
//			}
//		});

//		log.info("TEST");

	}

	@Override
	public IRhenaModule materialiseModel(String component, String module, String version) throws RhenaException {

		return context.materialiseModel(ModuleIdentifier.valueOf(component + ":" + module + ":" + version));
	}
}
