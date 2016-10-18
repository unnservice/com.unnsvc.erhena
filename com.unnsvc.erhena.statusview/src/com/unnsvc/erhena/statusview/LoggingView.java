
package com.unnsvc.erhena.statusview;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.erhena.statusview.log.LogContentProvider;
import com.unnsvc.erhena.statusview.log.LoggingViewTable;
import com.unnsvc.erhena.statusview.modules.AllEntry;
import com.unnsvc.erhena.statusview.modules.CoreEntry;
import com.unnsvc.erhena.statusview.modules.ModuleEntry;
import com.unnsvc.erhena.statusview.modules.ModuleViewContentProvider;
import com.unnsvc.erhena.statusview.modules.ModuleViewTable;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent.EAddRemove;
import com.unnsvc.rhena.core.logging.LogEvent;

public class LoggingView extends ViewPart {

	// @Inject
	// private IEventBroker broker;
	private ModuleViewTable moduleViewTable;
	private ModuleViewContentProvider moduleViewContentProvider;
	private LoggingViewTable logViewTable;
	private LogContentProvider logViewContentProvider;
	private LoggingViewFilter viewFilter;

	public LoggingView() {

		this.logViewContentProvider = new LogContentProvider();
		this.moduleViewContentProvider = new ModuleViewContentProvider();
		this.viewFilter = new LoggingViewFilter();
	}

	@Override
	public void createPartControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());

		System.err.println("Creating view");

		createMain(new Composite(container, SWT.NONE));
	}

	private void createMain(Composite parent) {

		parent.setLayout(new FillLayout());

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);

		createModuleList(sashForm);

		Composite tableContainer = new Composite(sashForm, SWT.BORDER);
		tableContainer.setLayout(new FillLayout());

		sashForm.setWeights(new int[] { 1, 2 });

		/**
		 * Should be able to begin creating stuff here
		 */
		createModuleLog(tableContainer);

		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	@Override
	public void setFocus() {

	}

	private void createModuleList(Composite composite) {

		moduleViewTable = new ModuleViewTable(composite, moduleViewContentProvider);
		moduleViewTable.getTableViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				ModuleEntry entry = (ModuleEntry) selection.getFirstElement();

				if (entry instanceof AllEntry) {
					logViewTable.getTableViewer().resetFilters();
				} else if (entry instanceof CoreEntry) {
					viewFilter.setFilterOn(null);
					logViewTable.getTableViewer().setFilters(viewFilter);
				} else {
					viewFilter.setFilterOn(entry.getIdentifier());
					logViewTable.getTableViewer().setFilters(viewFilter);
				}
			}
		});

		moduleViewTable.getTableViewer().setInput(moduleViewContentProvider.getActiveModules());
	}

	private void createModuleLog(Composite composite) {

		logViewTable = new LoggingViewTable(composite, logViewContentProvider);
		logViewTable.getTableViewer().setInput(logViewContentProvider.getLogEvents());
	}

	@Inject
	@Optional
	private void subscribeLogEvent(@UIEventTopic(ErhenaConstants.TOPIC_LOGEVENT) LogEvent logEvent) {

		logViewContentProvider.addElement(logEvent);
		logViewTable.refresh();
	}

	@Inject
	@Optional
	private void subscribeModuleAddRemoveEvent(@UIEventTopic(ErhenaConstants.TOPIC_MODULE_ADDREMOVE) ModuleAddRemoveEvent moduleAddRemove) {

		if (moduleAddRemove.getAddRemove() == EAddRemove.ADDED) {
			moduleViewContentProvider.addElement(moduleAddRemove.getIdentifier());
		} else if (moduleAddRemove.getAddRemove() == EAddRemove.REMOVED) {
			moduleViewContentProvider.removeElement(moduleAddRemove.getIdentifier());
		}

		moduleViewTable.refresh();
	}

}
