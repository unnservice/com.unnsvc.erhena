
package com.unnsvc.erhena.core.builder;

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

import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.platform.service.RhenaPlatformService;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class RhenaBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.unnsvc.erhena.core.builder.RhenaBuilder";

	// @Inject
	// private IEventBroker eventBroker;
	// @Inject
	// private IRhenaService rhenaService;
	@Inject
	private RhenaPlatformService rhenaService;

	public RhenaBuilder() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

		System.err.println("Running build");
		
		// eventBroker.post(ErhenaConstants.TOPIC_LOGEVENT, data)

		// try {
		// handleResources(getProject());
		// } catch (RhenaException re) {
		// throw new CoreException(new Status(IStatus.ERROR,
		// Activator.PLUGIN_ID, re.getMessage(), re));
		// }

		try {
			IRhenaModule module = rhenaService.materialiseModel("com.test", "com.test2", "0.0.1");
		} catch (RhenaException re) {
			re.printStackTrace();
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, re.getMessage(), re));
		}

		// IRhenaModule model =
		// context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1"));
		//
		// EExecutionType type = EExecutionType.PROTOTYPE;
		// IRhenaEdge entryPointEdge = new RhenaEdge(type, model,
		// TraverseType.SCOPE);
		//
		// GraphResolver graphResovler = new GraphResolver(context);
		// graphResovler.resolveReferences(entryPointEdge);
		//
		// new ParallelGraphProcessor(context).processEdges(context.getEdges());

		return null;
	}

	private void handleResources(IProject project) throws RhenaException {

		// RhenaConfiguration config = new RhenaConfiguration();
		//
		// IRhenaContext resolver = new CachingResolutionContext(config);
		// resolver.getRepositories().add(new WorkspaceRepository(resolver, new
		// File(ResourcesPlugin.getWorkspace().getRoot().getLocationURI())));
		//
		// ModuleIdentifier entryPointIdentifier =
		// ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1");
		// IRhenaModule model = resolver.materialiseModel(entryPointIdentifier);
		//
		// EExecutionType type = EExecutionType.PROTOTYPE;
		// IRhenaEdge entryPointEdge = new RhenaEdge(type, model,
		// TraverseType.SCOPE);
		// GraphResolver graphResovler = new GraphResolver(resolver);
		// graphResovler.resolveReferences(entryPointEdge);
		//
		// new
		// ParallelGraphProcessor(resolver).processEdges(resolver.getEdges());
	}

}
