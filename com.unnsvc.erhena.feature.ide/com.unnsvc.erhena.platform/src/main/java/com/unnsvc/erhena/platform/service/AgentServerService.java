package com.unnsvc.erhena.platform.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IAgentServerService;

@Component(service = IAgentServerService.class)
public class AgentServerService implements IAgentServerService {

	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Activate
	public void activate() {
		
		log.info("Activate");
	}

	@Override
	public void startupServer() throws ErhenaException {

		log.info("Start server");
	}
}
