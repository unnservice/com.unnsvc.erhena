
package com.unnsvc.erhena.core.builder;

import java.io.File;
import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.erhena.common.InjectionHelper;
import com.unnsvc.erhena.common.services.IPlatformService;
import com.unnsvc.erhena.common.services.IProjectService;
import com.unnsvc.erhena.core.Activator;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.model.ModelHelper;

public class RhenaBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.unnsvc.erhena.core.builder.RhenaBuilder";

	private Logger log = LoggerFactory.getLogger(getClass());
	// @Inject
	// private IEventBroker eventBroker;
	@Inject
	private IPlatformService platformService;
	@Inject
	private IProjectService projectService;

	public RhenaBuilder() {

		InjectionHelper.inject(Activator.class, this);
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

		log.info("Executing build on project with monitor: " + monitor.hashCode());
		log.info("Project " + projectService + " platform " + platformService);
		// find roots

		try {

			IRhenaEngine engine = platformService.createEngine();
			URI projectLocation = getProject().getLocationURI();
			File projectPath = new File(projectLocation.getPath());

			ModuleIdentifier identifier = ModelHelper.locationToModuleIdentifier(projectPath);
			IRhenaModule module = engine.resolveModule(identifier);
			engine.resolveExecution(EExecutionType.ITEST, module);
		} catch (RhenaException e) {

			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}

		return null;
	}

}