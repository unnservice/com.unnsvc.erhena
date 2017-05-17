
package com.unnsvc.erhena.core.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.core.IJavaProject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.core.Activator;
import com.unnsvc.rhena.common.IRhenaEngine;

public class RhenaBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.unnsvc.erhena.core.builder.RhenaBuilder";

	// @Inject
	// private IEventBroker eventBroker;
	// @Inject
	// private IPlatformService platformService;
	// @Inject
	// private IProjectService projectService;

	public RhenaBuilder() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

		System.err.println("Executing build on project with monitor: " + monitor.hashCode());
		
		// IProject project = getProject();
		// IJavaProject javaProject = JavaCore.create(project);
		//
		// try {
		// platformService.newTransaction(new IRhenaTransaction() {
		//
		// @Override
		// public void execute(IRhenaEngine engine) throws Throwable {
		//
		// if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
		// // clean build
		//
		// } else {
		// // incremental, full, and auto
		// // only care about relevant changes (that aren't to the output
		// directory or to module.xml)
		// BuildDeltaTracker tracker = isRelevantDelta(getDelta(getProject()));
		// if (!tracker.getResources().isEmpty()) {
		// fullBuild(javaProject, engine);
		// }
		// }
		// }
		// });
		// } catch (Throwable t) {
		//
		// throw new CoreException(new Status(IStatus.ERROR,
		// Activator.PLUGIN_ID, t.getMessage(), t));
		// }

		return null;
	}

	private BuildDeltaTracker isRelevantDelta(IResourceDelta delta) throws CoreException {

		/**
		 * @TODO evaluate if we can allow dynamic lookup of output directories
		 */

		BuildDeltaTracker tracker = new BuildDeltaTracker(getProject());

		if (delta != null) {
			delta.accept(tracker);
		}

		return tracker;
	}

	private void fullBuild(IJavaProject javaProject, IRhenaEngine engine) throws Throwable {

		// ModuleIdentifier projectIdentifier =
		// projectService.manageProject(getProject());
		// engine.materialiseModel(projectIdentifier);
		//
		// // Build all parents in the workspace model
		// for (ModuleIdentifier root : engine.findRoots(projectIdentifier,
		// EExecutionType.TEST)) {
		//
		// System.err.println("Building project " + root);
		//
		// IRhenaModule rootModule = engine.materialiseModel(root);
		// engine.materialiseExecution(new Caller(rootModule,
		// EExecutionType.TEST));
		// }

		// WorkspaceExecution execution = (WorkspaceExecution)
		// engine.materialiseExecution(new Caller(module, EExecutionType.TEST));
		// System.err.println("Produced workspace execution: " + execution);
		//
		// eventBroker.post(ProfilerDiagnosticsEvent.TOPIC, new
		// ProfilerDiagnosticsEvent(platformService.getDiagnostics()));
		//
		// /**
		// * Drop the model and execution after we're done?
		// */
		// configureProject(javaProject, module, execution);
	}

}