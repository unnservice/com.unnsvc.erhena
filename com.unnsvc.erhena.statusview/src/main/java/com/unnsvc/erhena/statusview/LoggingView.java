
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
import com.unnsvc.erhena.statusview.log.LoggingViewTable;
import com.unnsvc.erhena.statusview.modules.AbstractModuleEntry;
import com.unnsvc.erhena.statusview.modules.ModuleViewTable;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.logging.LogEvent;

public class LoggingView extends ViewPart {

	// @Inject
	// private IEventBroker broker;
	private ModuleViewTable moduleViewTable;
	private LoggingViewTable logViewTable;

	public LoggingView() {

	}

	@Override
	public void createPartControl(Composite parent) {

		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		parent.setLayout(gl);

		createActionBar();

		Composite container = new Composite(parent, SWT.NONE);
		GridData containerData = new GridData();
		containerData.horizontalAlignment = SWT.FILL;
		containerData.verticalAlignment = SWT.FILL;
		containerData.grabExcessHorizontalSpace = true;
		containerData.grabExcessVerticalSpace = true;
		container.setLayoutData(containerData);
		createLoggingtables(container);

//		Composite statusBar = new Composite(parent, SWT.NONE);
//		GridData statusBarData = new GridData();
//		statusBarData.horizontalAlignment = SWT.FILL;
//		statusBarData.grabExcessHorizontalSpace = true;
//		statusBarData.grabExcessVerticalSpace = false;
//		statusBar.setLayoutData(statusBarData);
//		createStatusbar(statusBar);

		// We don't want events until the entire UI is created..
		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
	}

	private void createActionBar() {

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
						logViewTable.setFilter(level);
					}
				});

				combo.select(2);
				
				return combo;
			}
		});
		getViewSite().getActionBars().updateActionBars();
	}

	private void createLoggingtables(Composite parent) {

		parent.setLayout(new FillLayout());

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);

		createModuleList(sashForm);

		createModuleLog(sashForm);

		sashForm.setWeights(new int[] { 1, 3 });
	}

	private void createStatusbar(Composite topBar) {

		Label metrics = new Label(topBar, SWT.NONE);
		metrics.setText("Average lifecycle execution time: 30ms");
	}

	@Override
	public void setFocus() {

	}

	private void createModuleList(SashForm parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		moduleViewTable = new ModuleViewTable(composite);
		moduleViewTable.getTableViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				AbstractModuleEntry entry = (AbstractModuleEntry) selection.getFirstElement();
				
				entry.reset();
				
				logViewTable.setFilter(entry);
				moduleViewTable.refresh();
			}
		});

		moduleViewTable.refresh();
	}

	private void createModuleLog(SashForm parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		logViewTable = new LoggingViewTable(composite);
	}

	@Inject
	@Optional
	private void subscribeLogEvent(@UIEventTopic(ErhenaConstants.TOPIC_LOGEVENT) LogEvent logEvent) {

		moduleViewTable.onLogEvent(logEvent);
		logViewTable.addLogEvent(logEvent);
	}

	@Inject
	@Optional
	private void subscribeModuleAddRemoveEvent(@UIEventTopic(ErhenaConstants.TOPIC_MODULE_ADDREMOVE) ModuleAddRemoveEvent moduleAddRemove) {

		moduleViewTable.onModule(moduleAddRemove);
	}

}
