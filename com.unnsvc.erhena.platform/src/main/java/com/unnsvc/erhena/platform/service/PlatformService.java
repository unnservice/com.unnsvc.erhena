
package com.unnsvc.erhena.platform.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;

import com.unnsvc.erhena.common.services.IPlatformService;

@Creatable
@Singleton
public class PlatformService implements IPlatformService {

	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Preference(nodePath = "my.plugin.id")
	private IEclipsePreferences preferences;

	public PlatformService() {

	}

	@PostConstruct
	public void postCreate() {

	}

	@PreDestroy
	public void preDestroy() {

	}

	public void test() {

		System.err.println("event broker: " + eventBroker);
		System.err.println("prefs " + preferences);
	}
}
