
package com.unnsvc.erhena.core.commands;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.widgets.Menu;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.core.Activator;

public class RunCommandDynamic extends ContributionItem {

//	@Inject
//	private IPlatformService platformService;
//	@Inject
//	private IProjectService projectService;

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
//
//		platformService.newTransaction(new IRhenaTransaction() {
//
//			@Override
//			public void execute(IRhenaEngine engine) throws Throwable {
//
//				try {
//					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//					IStructuredSelection selection = (IStructuredSelection) page.getSelection();
//
//					Object selObject = selection.getFirstElement();
//					IProject resource = (IProject) Platform.getAdapterManager().getAdapter(selObject, IProject.class);
//
//					ModuleIdentifier identifier = projectService.manageProject(resource);
//					IRhenaModule module = engine.materialiseModel(identifier);
//
//					ILifecycleReference reference = module.getMergedLifecycleDeclarations(engine.getContext().getCache()).get(module.getLifecycleName());
//					if (!reference.getCommands().isEmpty()) {
//						for (ILifecycleCommandReference command : reference.getCommands()) {
//							MenuItem menuItem = new MenuItem(menu, SWT.NONE, index);
//							menuItem.setText("lifecycle[" + reference.getName() + "]->" + command.getCommandName());
//							menuItem.addSelectionListener(new SelectionAdapter() {
//
//								@Override
//								public void widgetSelected(SelectionEvent e) {
//
//									MenuItem item = (MenuItem) e.getSource();
//									String command = item.getText().split(Pattern.quote("]->"), 2)[1];
//
//									try {
//										// Need to get the module again because
//										// by the time we enter this listener,
//										// the transaction is completed and the
//										// context is cleared, so the module
//										// reference doesn't have a lifecycle
//										// from the cache, so we need to get it
//										// again
//										engine.getContext().getCache().getExecutions().remove(module.getIdentifier());
//										IRhenaModule module = engine.materialiseModel(identifier);
//										engine.materialiseExecution(new CommandCaller(module, EExecutionType.TEST, command));
//									} catch (RhenaException exception) {
//
//										try {
//											platformService.getRhenaLogger().fireLogEvent(ELogLevel.ERROR, getClass(), identifier, exception.getMessage(),
//													exception);
//										} catch (RhenaException e1) {
//
//											e1.printStackTrace();
//										}
//									}
//
//								}
//							});
//						}
//					}
//				} catch (Exception ex) {
//
//					ex.printStackTrace();
//				}
//			}
//		});
	}

}
