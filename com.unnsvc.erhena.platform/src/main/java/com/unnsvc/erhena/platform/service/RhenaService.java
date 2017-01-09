
package com.unnsvc.erhena.platform.service;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.events.LogEvent;
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
public class RhenaService implements IRhenaService {

	/**
	 * Single context throughout the application
	 */
	private IRhenaConfiguration config;
	private IRhenaContext context;
	private IRhenaEngine engine;
	// module => projectName tracking

	@Inject
	public RhenaService(IEventBroker eventBorker) {

		this.config = new RhenaConfiguration();
		config.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		config.setRunTest(true);
		config.setRunItest(true);
		config.setParallel(false);
		config.setPackageWorkspace(false);
		config.setInstallLocal(true);

		this.context = new RhenaContext(config);
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../../")));
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../")));
		context.setLocalRepository(new LocalCacheRepository(context));
		context.getListenerConfig().addListener(new IContextListener<LogEvent>() {

			@Override
			public Class<LogEvent> getType() {

				return LogEvent.class;
			}

			@Override
			public void onEvent(LogEvent evt) throws RhenaException {

				eventBorker.post(ErhenaConstants.TOPIC_LOGEVENT, evt);
			}
		});

		// context.addListener(new ProjectConfigurationHandler(this, context));

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		context.addWorkspaceRepository(new WorkspaceRepository(context, workspacePath));

		engine = new RhenaEngine(context);
	}

	/**
	 * A fake transaction until transaction method is established; this will
	 * take care of cleaning the caches
	 * 
	 * @param transaction
	 * @throws Throwable
	 *             Any exception
	 */
	public void newTransaction(IRhenaTransaction transaction) throws Throwable {

		/**
		 * @TODO context is AutoClosable so use it like that in try(Context)
		 */
		try {
			transaction.execute(engine);
		} finally {
			
			engine.getContext().close();
			
//			// @TODO auto closable context so we wont need this after code refactoring
//			engine.getContext().getCache().getExecutions().clear();
//			engine.getContext().getCache().getLifecycles().clear();
//			
//			// Only remove lifecycles and executions
//			
//			engine.getContext().getCache().getModules().clear();
//			engine.getContext().getCache().getEdges().clear();
//			engine.getContext().getCache().getMerged().clear();
		}
	}

	public IRhenaExecution buildProject(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = engine.materialiseModel(identifier);
		IRhenaExecution execution = engine.materialiseExecution(module, EExecutionType.TEST);
		return execution;
	}

	public IRhenaEngine getEngine() {

		return engine;
	}

	public ILogger getRhenaLogger() {

		return context.getLogger();
	}
}
