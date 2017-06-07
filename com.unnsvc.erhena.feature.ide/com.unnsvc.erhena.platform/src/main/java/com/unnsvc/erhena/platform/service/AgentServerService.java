
package com.unnsvc.erhena.platform.service;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IAgentServerService;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.agent.RhenaAgent;
import com.unnsvc.rhena.common.IRhenaAgent;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.ObjectServerException;
import com.unnsvc.rhena.objectserver.server.ObjectServerHelper;

@Component(service = IAgentServerService.class, scope = ServiceScope.SINGLETON)
public class AgentServerService implements IAgentServerService {

	private Logger log = LoggerFactory.getLogger(getClass());
	@Reference
	private IConfigurationService configService;
	private IRhenaAgent agent;

	@Activate
	public void activate() throws RhenaException, ObjectServerException {

		log.info("Activating, starting agent");
		IRhenaConfiguration config = configService.getConfig();
		if (config.getAgentConfiguration().getAgentAddress() == null) {
			agent = new RhenaAgent(ObjectServerHelper.availableAddress());
		} else {
			agent = new RhenaAgent(config.getAgentConfiguration().getAgentAddress());
		}
		agent.start();
	}

	@Deactivate
	public void deactivate() {

	}

	@Override
	public void startupServer() throws ErhenaException {

		log.info("Start server");
	}
}
