
package com.unnsvc.erhena.statusview;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.erhena.statusview.log.LoggingViewTable;
import com.unnsvc.erhena.statusview.modules.AbstractModuleEntry;
import com.unnsvc.erhena.statusview.modules.ModuleViewTable;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.events.LogEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;

public class LoggingView extends ViewPart {

	// @Inject
	// private IEventBroker broker;
	private ModuleViewTable moduleViewTable;
	private LoggingViewTable logViewTable;

	public LoggingView() {

	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		container.setLayout(gl);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 1;
		Text text = new Text(container, SWT.SEARCH);
		text.setText("Search is not implemented yet");
		text.setEnabled(false);
		text.setLayoutData(data);

		GridData loglevelData = new GridData();
		loglevelData.horizontalSpan = 1;
		final Combo loglevel = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		List<String> logLevels = new ArrayList<String>();
		for (ELogLevel l : ELogLevel.values()) {
			logLevels.add(l.name().toLowerCase());
		}
		loglevel.setItems(logLevels.toArray(new String[logLevels.size()]));
		loglevel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				ELogLevel level = ELogLevel.values()[loglevel.getSelectionIndex()];
				logViewTable.setFilter(level);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

				ELogLevel level = ELogLevel.values()[loglevel.getSelectionIndex()];
				logViewTable.setFilter(level);
			}
		});
		loglevel.setLayoutData(loglevelData);

		

		Composite seashContainer = new Composite(container, SWT.NONE);
		GridData data2 = new GridData(GridData.FILL_BOTH);
		data2.horizontalSpan = 2;
		// data2.grabExcessHorizontalSpace = true;
		// data2.grabExcessVerticalSpace = true;
		seashContainer.setLayoutData(data2);
		createLoggingtables(seashContainer);

		// We don't want events until the entire UI is created..
		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(this, eclipseContext);
		
		
		
		// Do this last
		loglevel.select(2);
	}

	private void createLoggingtables(Composite parent) {

		parent.setLayout(new FillLayout());

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);

		createModuleList(sashForm);

		createModuleLog(sashForm);

		sashForm.setWeights(new int[] { 1, 3 });
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

/**
 * 
 * private void createStatusbar(Composite topBar) {
 * 
 * Label metrics = new Label(topBar, SWT.NONE); metrics.setText("Average
 * lifecycle execution time: 30ms"); }
 * 
 * // createActionBar(); // Composite statusBar = new Composite(parent,
 * SWT.NONE); // GridData statusBarData = new GridData(); //
 * statusBarData.horizontalAlignment = SWT.FILL; //
 * statusBarData.grabExcessHorizontalSpace = true; //
 * statusBarData.grabExcessVerticalSpace = false; //
 * statusBar.setLayoutData(statusBarData); // createStatusbar(statusBar);
 * private void createActionBar() {
 * 
 * // getViewSite().getActionBars().getToolBarManager().add(new //
 * GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS)); //
 * getViewSite().getActionBars().getToolBarManager().prependToGroup("additions",
 * // new Something("someid")); //$NON-NLS-1$
 * 
 * List<String> values = new ArrayList<String>(); for (ELogLevel log :
 * ELogLevel.values()) { values.add(log.toString()); }
 * 
 * getViewSite().getActionBars().getToolBarManager().add(new
 * ControlContribution("something") {
 * 
 * @Override protected Control createControl(Composite parent) {
 * 
 *           ToolBar toolBar = new ToolBar(parent, SWT.FLAT |SWT.BORDER);
 * 
 *           Device dev = toolBar.getDisplay();
 * 
 * 
 *           ToolItem item0 = new ToolItem (toolBar, SWT.PUSH); //
 *           item0.setImage(newi); item0.setText("Hello");
 * 
 *           ToolItem item1 = new ToolItem(toolBar, SWT.PUSH);
 *           item1.setText("Push");
 * 
 *           ToolItem item2 = new ToolItem(toolBar, SWT.PUSH);
 *           item2.setText("Pull");
 * 
 * 
 *           return toolBar; }
 * 
 *           // @Override // protected Control createControl(Composite parent) {
 *           // // Composite c = new Composite(parent, SWT.NONE); // // Combo
 *           combo = new Combo(c, SWT.READ_ONLY); // combo.setItems("TRACE",
 *           "DEBUG", "INFO", "WARN", "ERROR"); ////
 *           combo.setItems(values.toArray(new String[values.size()])); //
 *           combo.addSelectionListener(new SelectionAdapter() { // // @Override
 *           // public void widgetDefaultSelected(SelectionEvent e) { // //
 *           super.widgetSelected(e); // } // // @Override // public void
 *           widgetSelected(SelectionEvent e) { // // ELogLevel level =
 *           ELogLevel.valueOf(combo.getText()); //
 *           logViewTable.setFilter(level); // } // }); // // combo.select(0);
 *           // //// ComboViewer comboViewer = new ComboViewer(combo, SWT.NONE |
 *           // SWT.READ_ONLY); // return c; // } });
 * 
 *           // getViewSite().getActionBars().updateActionBars(); //
 *           getViewSite().getActionBars().getToolBarManager().update(true); }
 * 
 **/