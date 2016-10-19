
package com.unnsvc.erhena.statusview;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
import com.unnsvc.rhena.common.logging.ELogLevel;
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

		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		parent.setLayout(gl);

		Composite container = new Composite(parent, SWT.NONE);
		GridData containerData = new GridData();
		containerData.horizontalAlignment = SWT.FILL;
		containerData.verticalAlignment = SWT.FILL;
		containerData.grabExcessHorizontalSpace = true;
		containerData.grabExcessVerticalSpace = true;
		container.setLayoutData(containerData);
		createMain(container);

		Composite topBar = new Composite(parent, SWT.NONE);
		GridData topBarData = new GridData();
		topBarData.horizontalAlignment = SWT.FILL;
		topBarData.grabExcessHorizontalSpace = true;
		topBarData.grabExcessVerticalSpace = false;
		topBar.setLayoutData(topBarData);
		createTopbar(topBar);

	}

	private void createTopbar(Composite topBar) {

		Label metrics = new Label(topBar, SWT.NONE);
		metrics.setText("Average lifecycle execution time: 30ms");
	}

	private void createMain(Composite parent) {

		// getViewSite().getActionBars().getToolBarManager().add(new
		// GroupMarker("additions")); //$NON-NLS-1$
		// getViewSite().getActionBars().getToolBarManager().prependToGroup("additions",
		// new Something("someid")); //$NON-NLS-1$
		getViewSite().getActionBars().getToolBarManager().add(new ControlContribution("something") {

			@Override
			protected Control createControl(Composite parent) {

				Combo combo = new Combo(parent, SWT.READ_ONLY);
				combo.setItems("TRACE", "DEBUG", "INFO", "WARN", "ERROR");

				combo.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {

						super.widgetSelected(e);
					}

					@Override
					public void widgetSelected(SelectionEvent e) {

						ELogLevel level = ELogLevel.valueOf(combo.getText());
						viewFilter.setLevel(level);
						logViewTable.getTableViewer().setFilters(viewFilter);
					}
				});

				combo.select(2);
				return combo;
			}
		});
		getViewSite().getActionBars().updateActionBars();

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

		// We don't want events until the entire UI is created..
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
