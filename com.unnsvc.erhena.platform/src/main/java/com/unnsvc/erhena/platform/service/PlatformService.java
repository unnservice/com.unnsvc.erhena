
package com.unnsvc.erhena.platform.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.osgi.service.component.annotations.Component;

@Component(name = "platformService", service = IPlatformService.class)
public class PlatformService implements IPlatformService {

	@PostConstruct
	public void postConstruct() {
		
		// created
	}
	
	@PreDestroy
	public void preDestroy() {
		
	}
}
