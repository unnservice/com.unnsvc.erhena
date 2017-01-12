
package com.unnsvc.erhena.platform.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
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
		config.setAgentClasspath(buildAgentClasspath());

		/**
		 * Workaround for ensuring that the rmi registry receives the right
		 * classpath
		 */
		this.context = rmiRegistryClasspathWorkaround(config);
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../../")));
		context.addWorkspaceRepository(new WorkspaceRepository(context, new File("../")));
		context.setLocalRepository(new LocalCacheRepository(context));
		context.getListenerConfig().addListener(new IContextListener<LogEvent>() {

			private static final long serialVersionUID = 1L;

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
	 * @TODO might need to find a different approach for this because this is
	 *       very ugly
	 * @param ctx
	 */
	private IRhenaContext rmiRegistryClasspathWorkaround(IRhenaConfiguration configuration) {

		System.err.println("Current classloader is: " + Thread.currentThread().getContextClassLoader());
		IRhenaContext context = null;
		try {

			context = doWithClassLoader(configuration.getClass().getClassLoader(), new Callable<IRhenaContext>() {

				@Override
				public IRhenaContext call() throws Exception {

					String original = System.getProperty("java.rmi.server.codebase");
					System.setProperty("java.rmi.server.codebase",
							"file:/data/storage/sources/com.unnsvc/com.unnsvc.rhena/com.unnsvc.rhena.common/target/classes/");
					// ctx.getLifecycleAgentManager();
					IRhenaContext context = new RhenaContext(config);
					if (original != null) {
						System.setProperty("java.rmi.server.codebase", original);
					}

					return context;
				}
			});
			return context;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return context;
	}

	public static <V> V doWithClassLoader(final ClassLoader classLoader, final Callable<V> callable) throws Exception {

		Thread currentThread = null;
		ClassLoader backupClassLoader = null;
		try {
			if (classLoader != null) {
				currentThread = Thread.currentThread();
				backupClassLoader = currentThread.getContextClassLoader();
				currentThread.setContextClassLoader(classLoader);
			}
			return callable.call();
		} finally {
			if (classLoader != null) {
				currentThread.setContextClassLoader(backupClassLoader);
			}
		}
	}

	private String buildAgentClasspath() {

		try {
			List<String> paths = new ArrayList<String>();
			paths.add(new File(FileLocator.resolve(new URL("platform:/plugin/com.unnsvc.rhena.agent/META-INF/MARKER")).toURI()).getAbsolutePath());
			paths.add(new File(FileLocator.resolve(new URL("platform:/plugin/com.unnsvc.rhena.common/META-INF/MARKER")).toURI()).getAbsolutePath());
			paths.add(new File(FileLocator.resolve(new URL("platform:/plugin/com.unnsvc.rhena.core/META-INF/MARKER")).toURI()).getAbsolutePath());
			paths.add(new File(FileLocator.resolve(new URL("platform:/plugin/com.unnsvc.rhena.lifecycle/META-INF/MARKER")).toURI()).getAbsolutePath());

			StringBuilder sb = new StringBuilder();
			for (String path : paths) {
				path = path.substring(0, path.length() - "/META-INF/MARKER".length()) + "/";
				System.err.println("Appending to classpath " + path);
				sb.append(path).append(File.pathSeparatorChar);
			}

			return sb.toString().substring(0, sb.toString().length() - 1);
		} catch (URISyntaxException | IOException e) {

			e.printStackTrace();
			return System.getProperty("java.class.path");
		}
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

			// engine.getContext().close();

			// @TODO auto closable context so we wont need this after code
			// refactoring
			engine.getContext().getCache().getExecutions().clear();
			engine.getContext().getCache().getLifecycles().clear();

			// Only remove lifecycles and executions

			engine.getContext().getCache().getModules().clear();
			engine.getContext().getCache().getEdges().clear();
			engine.getContext().getCache().getMerged().clear();
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
