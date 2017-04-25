
package com.unnsvc.erhena.platform.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.prefs.BackingStoreException;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IPlatformService;
import com.unnsvc.erhena.preferences.PreferencesActivator;
import com.unnsvc.rhena.common.IRhenaEngine;

@Creatable
@Singleton
public class PlatformService implements IPlatformService {

	@Inject
	private IEventBroker eventBroker;
	/**
	 * @TODO add node change listener so platform resarts if RHENA_HOME becomes
	 *       configured or something
	 */
	@Inject
	@Preference(nodePath = PreferencesActivator.PLUGIN_ID)
	private IEclipsePreferences preferences;

//	private IRhenaAgentClient client;
	private IRhenaEngine engine;

	public PlatformService() {

	}

	@PostConstruct
	public void postCreate() throws BackingStoreException {

	}

	@PreDestroy
	public void preDestroy() throws ErhenaException {

//		if (client != null) {
//			try {
//				client.shutdown();
//			} catch (RhenaException re) {
//				throw new ErhenaException(re);
//			}
//		}
	}

	/**
	 * @TODO Locates platform, it first checks preference node to see whether a
	 *       platform is defined, if it is then spawn that, otherwise spawn a
	 *       new platform
	 * 
	 * @return
	 */
	@Override
	public IRhenaEngine locatePlatform() throws ErhenaException {

//		if (engine != null) {
//			return engine;
//		} else {
//			try {
//				String rhenaHomeLocation = preferences.get(RhenaConfigurationConstants.P_RHENA_HOME, null);
//				if (rhenaHomeLocation != null) {
//
//					throw new UnsupportedOperationException("Custom runtimes not implemented");
//				} else {
//
//					this.client = new AgentClient(AgentServerProcess.AGENT_EXECUTION_PORT);
//
//					IRhenaConfiguration config = new RhenaConfiguration();
//
//					IRhenaContext context = new RhenaContext(config);
//					context.setAgent(client);
//
//					IRhenaEngine engine = new RhenaEngine(context);
//					this.engine = engine;
//					return engine;
//				}
//			} catch (RhenaException re) {
//
//				throw new ErhenaException(re);
//			}
//		}
		throw new UnsupportedOperationException("Not implemented");
	}

	public void test() {

		System.err.println("event broker: " + eventBroker);
		System.err.println("prefs " + preferences);
	}
}
