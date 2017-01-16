
package com.unnsvc.erhena.statusview;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.unnsvc.erhena.common.ErhenaConstants;
import com.unnsvc.erhena.common.events.AgentProcessStartExitEvent;
import com.unnsvc.erhena.common.events.ProfilerDiagnosticsEvent;
import com.unnsvc.erhena.statusview.log.LoggingViewTable;
import com.unnsvc.erhena.statusview.modules.AbstractModuleEntry;
import com.unnsvc.erhena.statusview.modules.ModuleViewTable;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.events.LogEvent;

public class LoggingView extends ViewPart {

	// @Inject
	// private IEventBroker broker;
	private ModuleViewTable moduleViewTable;
	private LoggingViewTable logViewTable;
	private Label agentStatusLabel;
	private Button resetAgentButton;
	private Button dumpAgentButton;
	private Combo loglevel;
	private Label agentStatus;

	public LoggingView() {

	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		container.setLayout(gl);

		createTopBar(container);

		Composite seashContainer = new Composite(container, SWT.NONE);
		GridData seashContainerData = new GridData(GridData.FILL_BOTH);
		seashContainerData.horizontalSpan = 3;
		// data2.grabExcessHorizontalSpace = true;
		// data2.grabExcessVerticalSpace = true;
		seashContainer.setLayoutData(seashContainerData);
		createLoggingtables(seashContainer);

		createLifecycleAgentStatusBar(container);

		// We don't want events until the entire UI is created..
		BundleContext bundleContext = FrameworkUtil.getBundle(Activator.class).getBundleContext();
		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(LoggingView.this, eclipseContext);
		
		// Do this last
		loglevel.select(2);
	}

	private void createTopBar(Composite container) {

		GridData searchFieldData = new GridData(GridData.FILL_HORIZONTAL);
		searchFieldData.grabExcessHorizontalSpace = true;
		searchFieldData.horizontalSpan = 1;
		Text searchField = new Text(container, SWT.SEARCH);
		searchField.setText("Search is not implemented yet");
		searchField.setEnabled(false);
		searchField.setLayoutData(searchFieldData);

		GridData loglevelData = new GridData();
		loglevelData.horizontalSpan = 1;
		this.loglevel = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
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

		GridData clearButtonData = new GridData();
		clearButtonData.horizontalSpan = 1;
		Button clearButton = new Button(container, SWT.PUSH);
		clearButton.setText("Clear");
		clearButton.setLayoutData(clearButtonData);
		clearButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				if (e.type == SWT.Selection) {
					moduleViewTable.clear();
					logViewTable.clear();
					moduleViewTable.refresh();
					logViewTable.refresh();
					System.out.println("Cleared log");
				}
			}
		});
	}

	private void createLifecycleAgentStatusBar(Composite container) {

		Composite statusBar = new Composite(container, SWT.NONE);
		GridData statusBarData = new GridData(GridData.FILL_HORIZONTAL);
		statusBarData.horizontalSpan = 3;
		// statusBarData.grabExcessHorizontalSpace = true;
		// statusBarData.grabExcessVerticalSpace = false;
		statusBar.setLayoutData(statusBarData);

		GridLayout layout = new GridLayout();
		layout.numColumns = 5;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		statusBar.setLayout(layout);

		GridData agentStatusLabelData = new GridData();
		agentStatusLabelData.widthHint = 70;
		this.agentStatusLabel = new Label(statusBar, SWT.NULL);
		agentStatusLabel.setText("Running");
		agentStatusLabel.setLayoutData(agentStatusLabelData);

		GridData separatorData = new GridData();
		separatorData.horizontalIndent = 5;
		Label separator = new Label(statusBar, SWT.SEPARATOR);
		separator.setLayoutData(separatorData);

		GridData agentStatusData = new GridData();
		agentStatusData.grabExcessHorizontalSpace = true;
		agentStatusData.horizontalIndent = 5;
		this.agentStatus = new Label(statusBar, SWT.NULL);
		agentStatus.setText("Waiting for agent diagnostics");
		agentStatus.setLayoutData(agentStatusData);

		this.resetAgentButton = new Button(statusBar, SWT.PUSH);
		resetAgentButton.setText("Restart");
		GridData resetData = new GridData();
		resetData.horizontalIndent = 5;
		resetAgentButton.setLayoutData(resetData);

		this.dumpAgentButton = new Button(statusBar, SWT.PUSH);
		dumpAgentButton.setText("Dump");
		GridData dumpData = new GridData();
		dumpData.horizontalIndent = 5;
		dumpAgentButton.setLayoutData(dumpData);
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

				// Might become null if it is removed, this event will still
				// fire
				if (entry != null) {
					entry.reset();

					logViewTable.setFilter(entry);
					moduleViewTable.refresh();
				}
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
	private void subscribeAgentProcessStartExitEvent(@UIEventTopic(AgentProcessStartExitEvent.TOPIC) AgentProcessStartExitEvent agentProcessStartExit) {

		if (agentProcessStartExit.getStartStop().equals(AgentProcessStartExitEvent.EStartStop.START)) {

			dumpAgentButton.setEnabled(true);
			agentStatusLabel.setText("Started");
		} else if (agentProcessStartExit.getStartStop().equals(AgentProcessStartExitEvent.EStartStop.STOP)) {

			dumpAgentButton.setEnabled(false);
			agentStatusLabel.setText("Stopped");
		}
	}

	@Inject
	@Optional
	private void subscribeDiagnosticsReport(@UIEventTopic(ProfilerDiagnosticsEvent.TOPIC) ProfilerDiagnosticsEvent diagEvent) {

		new UIJob(this.agentStatus.getDisplay(), "") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {

				agentStatus.setText(diagEvent.getReport().getTotalLoadedClasses() + " classes in lifecycle runtime");
				return new Status(IStatus.OK, Activator.PLUGIN_ID, "");
			}
		}.schedule();
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