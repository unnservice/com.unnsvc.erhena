
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
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

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

	// @Inject
	// private IEventBroker eventBroker;
	@Inject
	private IPlatformService platformService;
	@Inject
	private IProjectService projectService;

	public RhenaBuilder() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

		System.err.println("Executing build on project with monitor: " + monitor.hashCode());
		System.err.println("Project" + projectService + " platform " + platformService);
		// find roots

		IRhenaEngine engine = platformService.createEngine();
		URI projectLocation = getProject().getLocationURI();
		File projectPath = new File(projectLocation.getPath());

		try {
			ModuleIdentifier identifier = ModelHelper.locationToModuleIdentifier(projectPath);
			IRhenaModule module = engine.resolveModule(identifier);
			engine.resolveExecution(EExecutionType.ITEST, module);
		} catch (RhenaException e) {

			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}

		return null;
	}

}