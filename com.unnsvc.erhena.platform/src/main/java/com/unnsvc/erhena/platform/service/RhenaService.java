
package com.unnsvc.erhena.platform.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.erhena.common.ErhenaUtils;
import com.unnsvc.erhena.common.events.AgentProcessStartExitEvent;
import com.unnsvc.rhena.common.ICaller;
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
import com.unnsvc.rhena.common.process.IProcessListener;
import com.unnsvc.rhena.common.visitors.IDependencies;
import com.unnsvc.rhena.core.Caller;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.events.LogEvent;
import com.unnsvc.rhena.core.resolution.Dependencies;
import com.unnsvc.rhena.core.resolution.LocalCacheRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.URLDependencyTreeVisitor;
import com.unnsvc.rhena.profiling.report.IDiagnosticReport;

/**
 * @TODO As the platform service handlest most eRhena oepartions, test it
 * @DEVNOTE services aren't @Creatable anymore because they need to be injected
 *          by interface
 * @author noname
 *
 */
@Singleton
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

		configureContext(eventBorker);
	}

	private void configureContext(IEventBroker eventBorker) {

		config = new RhenaConfiguration();
		config.setRhenaHome(new File(System.getProperty("user.home"), ".rhena"));
		config.setRunTest(true);
		config.setRunItest(true);
		config.setParallel(false);
		config.setPackageWorkspace(false);
		config.setInstallLocal(true);
		config.setAgentClasspath(buildAgentClasspath());
		config.getAgentStartListeners().add(new IProcessListener() {

			@Override
			public void onProcess(Process process) {

				eventBorker.post(AgentProcessStartExitEvent.TOPIC, new AgentProcessStartExitEvent(AgentProcessStartExitEvent.EStartStop.START));
			}
		});
		config.getAgentExitListeners().add(new IProcessListener() {

			@Override
			public void onProcess(Process process) {

				eventBorker.post(AgentProcessStartExitEvent.TOPIC, new AgentProcessStartExitEvent(AgentProcessStartExitEvent.EStartStop.STOP));
			}
		});

		try {
			config.setProfilerClasspath(buildProfilerClasspath());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/**
		 * Workaround for ensuring that the rmi registry receives the right
		 * classpath
		 */
		context = rmiRegistryClasspathWorkaround(config);
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

		// context.addListener(new ProjectConfigurationHandler(this,
		// context));

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspacePath = new File(workspaceRoot.getLocationURI());
		context.addWorkspaceRepository(new WorkspaceRepository(context, workspacePath));

		engine = new RhenaEngine(context);
	}

	private String buildProfilerClasspath() throws MalformedURLException, URISyntaxException, IOException {

		String path = ErhenaUtils.locateClasspath("com.unnsvc.rhena.profiling");
		File pathLocation = new File(path);
		/**
		 * One-off where when we run erhena from the IDE, it uses exploded
		 * directories so we need to point it to the jar
		 */
		if (pathLocation.isDirectory()) {
			path = new File(pathLocation, "../com.unnsvc.rhena.profiling-0.0.1-SNAPSHOT.jar").getCanonicalFile().getAbsolutePath();
			System.err.println("javaagent classpath was directory, one-off hack setting to " + path);
		}

		return path;
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
			paths.add(ErhenaUtils.locateClasspath("com.unnsvc.rhena.agent"));
			paths.add(ErhenaUtils.locateClasspath("com.unnsvc.rhena.common"));
			paths.add(ErhenaUtils.locateClasspath("com.unnsvc.rhena.core"));
			paths.add(ErhenaUtils.locateClasspath("com.unnsvc.rhena.lifecycle"));

			StringBuilder sb = new StringBuilder();
			for (String path : paths) {
				System.err.println("Appending to classpath " + path);
				sb.append(path).append(File.pathSeparatorChar);
			}

			String agentClasspath = sb.toString().substring(0, sb.toString().length() - 1);
			System.err.println("Agent claspath: " + agentClasspath);
			return agentClasspath;
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
	@Override
	public void newTransaction(IRhenaTransaction transaction) {

		try {
			transaction.execute(engine);
		} catch (Throwable e) {

			e.printStackTrace();
		} finally {

			// engine.getContext().close();

			// @TODO auto closable context so we wont need this
			// after code
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
		ICaller caller = new Caller(module, EExecutionType.TEST);
		IRhenaExecution execution = engine.materialiseExecution(caller);
		return execution;
	}

	@Override
	public ILogger getRhenaLogger() {

		return context.getLogger();
	}

	public IDiagnosticReport getDiagnostics() throws RhenaException {

		try {
			IDiagnosticReport report = context.getLifecycleAgentManager().getAgentReport();
			System.err.println("Got report " + report);
			return report;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	@Override
	public IDependencies collectDependencies(IRhenaModule module, EExecutionType type) throws RhenaException {

		URLDependencyTreeVisitor deptree = new URLDependencyTreeVisitor(engine.getContext().getCache(), type);
		module.visit(deptree);
		System.err.println("Collected dependencies for "+ module.getIdentifier() + " to " + deptree.getDependencies());
		return deptree.getDependencies();
	}
}
