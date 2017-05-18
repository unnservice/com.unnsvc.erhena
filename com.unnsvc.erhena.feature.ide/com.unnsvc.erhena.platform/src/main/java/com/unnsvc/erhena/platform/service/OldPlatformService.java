
package com.unnsvc.erhena.platform.service;

//@Creatable
//@Singleton
//@SuppressWarnings("restriction")
public class OldPlatformService {
	//implements IPlatformService {

//
//	@Inject
//	private IEventBroker eventBroker;
//	/**
//	 * @TODO add node change listener so platform resarts if RHENA_HOME becomes
//	 *       configured or something
//	 */
//	@Inject
//	@Preference(nodePath = PreferencesActivator.PLUGIN_ID)
//	private IEclipsePreferences preferences;
//
////	private IRhenaAgentClient client;
//	private IRhenaEngine engine;
//
//	public OldPlatformService() {
//
//	}
//
//	@PostConstruct
//	public void postCreate() throws BackingStoreException {
//
//	}
//
//	@PreDestroy
//	public void preDestroy() throws ErhenaException {
//
////		if (client != null) {
////			try {
////				client.shutdown();
////			} catch (RhenaException re) {
////				throw new ErhenaException(re);
////			}
////		}
//	}
//
//	/**
//	 * @TODO Locates platform, it first checks preference node to see whether a
//	 *       platform is defined, if it is then spawn that, otherwise spawn a
//	 *       new platform
//	 * 
//	 * @return
//	 */
//	@Override
//	public IRhenaEngine locatePlatform() throws ErhenaException {
//
////		if (engine != null) {
////			return engine;
////		} else {
////			try {
////				String rhenaHomeLocation = preferences.get(RhenaConfigurationConstants.P_RHENA_HOME, null);
////				if (rhenaHomeLocation != null) {
////
////					throw new UnsupportedOperationException("Custom runtimes not implemented");
////				} else {
////
////					this.client = new AgentClient(AgentServerProcess.AGENT_EXECUTION_PORT);
////
////					IRhenaConfiguration config = new RhenaConfiguration();
////
////					IRhenaContext context = new RhenaContext(config);
////					context.setAgent(client);
////
////					IRhenaEngine engine = new RhenaEngine(context);
////					this.engine = engine;
////					return engine;
////				}
////			} catch (RhenaException re) {
////
////				throw new ErhenaException(re);
////			}
////		}
//		
//		
//		
//		throw new UnsupportedOperationException("Not implemented");
//	}
//
//	public void test() {
//
//		System.err.println("event broker: " + eventBroker);
//		System.err.println("prefs " + preferences);
//	}
}
