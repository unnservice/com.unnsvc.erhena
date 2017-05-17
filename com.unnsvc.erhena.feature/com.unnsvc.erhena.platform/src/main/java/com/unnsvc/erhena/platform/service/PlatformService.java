
package com.unnsvc.erhena.platform.service;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.osgi.service.component.annotations.Component;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.config.RepositoryDefinition;
import com.unnsvc.rhena.config.RhenaConfiguration;
import com.unnsvc.rhena.core.RhenaCache;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.RhenaEngine;
import com.unnsvc.rhena.core.RhenaFactories;
import com.unnsvc.rhena.repository.RhenaResolver;

@Component(service = IPlatformService.class)
public class PlatformService implements IPlatformService {

	private IRhenaContext context;
	private IRhenaEngine engine;

	@PostConstruct
	public void postConstruct() {

		// Configure a platform from eclipse settings?
		IRhenaConfiguration config = new RhenaConfiguration();
		configureRepositories(config);
		IRhenaCache cache = new RhenaCache();
		IRhenaResolver resolver = new RhenaResolver();
		IRhenaFactories factories = new RhenaFactories();
		this.context = new RhenaContext(config, cache, resolver, factories);
		this.engine = new RhenaEngine(context);
	}

	private void configureRepositories(IRhenaConfiguration config) {

		URI workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocationURI();
		RepositoryIdentifier identifier = new RepositoryIdentifier("test");
		RepositoryDefinition repository = new RepositoryDefinition(ERepositoryType.WORKSPACE, identifier, workspaceRoot);
		config.getRepositoryConfiguration().addWorkspaceRepositories(repository);
	}

	@PreDestroy
	public void preDestroy() {

		
	}

	@Override
	public IRhenaContext buildProject(IProject project) {

		return context;
	}
}
