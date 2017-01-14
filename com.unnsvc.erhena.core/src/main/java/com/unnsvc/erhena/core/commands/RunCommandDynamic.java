
package com.unnsvc.erhena.core.commands;

import java.util.regex.Pattern;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.platform.service.ProjectService;
import com.unnsvc.erhena.platform.service.RhenaService;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleCommandReference;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.CommandCaller;

public class RunCommandDynamic extends ContributionItem {

	@Inject
	private RhenaService platformService;
	@Inject
	private ProjectService projectService;

	public RunCommandDynamic() {

		inject();
	}

	private void inject() {

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	public RunCommandDynamic(String id) {

		super(id);

		inject();
	}

	@Override
	public void fill(Menu menu, int index) {

		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IStructuredSelection selection = (IStructuredSelection) page.getSelection();

			Object selObject = selection.getFirstElement();
			IProject resource = (IProject) Platform.getAdapterManager().getAdapter(selObject, IProject.class);

			ModuleIdentifier identifier = projectService.manageProject(resource);
			IRhenaModule module = platformService.getEngine().materialiseModel(identifier);

			ILifecycleReference reference = module.getLifecycleDeclarations().get(module.getLifecycleName());
			if (!reference.getCommands().isEmpty()) {
				for (ILifecycleCommandReference command : reference.getCommands()) {
					MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
					menuItem.setText("lifecycle[" + reference.getName() + "]->" + command.getCommandName());
					menuItem.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(SelectionEvent e) {

							MenuItem item = (MenuItem) e.getSource();
							String command = item.getText().split(Pattern.quote("]->"), 2)[1];

							try {
								platformService.getEngine().getContext().getCache().getExecutions().remove(module.getIdentifier());
								platformService.getEngine().materialiseExecution(new CommandCaller(module, EExecutionType.TEST, command));
							} catch (RhenaException exception) {
								
								try {
									platformService.getRhenaLogger().fireLogEvent(ELogLevel.ERROR, RunCommandDynamic.class, identifier, exception.getMessage(), exception);
								} catch (RhenaException e1) {
									e1.printStackTrace();
								}
							}
						}
					});
				}
			}

			// super.fill(menu, index);
			// MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
			// menuItem.setText("My menu item (" + new Date() + ")");
			// menuItem.addSelectionListener(new SelectionAdapter() {
			//
			// public void widgetSelected(SelectionEvent e) {
			//
			// // what to do when menu is subsequently selected.
			// System.err.println("Dynamic menu selected");
			// }
			// });
		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

}
