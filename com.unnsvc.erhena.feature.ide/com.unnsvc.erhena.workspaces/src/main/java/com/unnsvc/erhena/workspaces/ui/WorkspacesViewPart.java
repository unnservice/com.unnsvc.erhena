
package com.unnsvc.erhena.workspaces.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.progress.UIJob;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IWorkspacesService;
import com.unnsvc.erhena.workspaces.Activator;

public class WorkspacesViewPart {

	private Button add;
	private Button del;
	private TableViewer tableViewer;

	@Inject
	private IWorkspacesService workspacesServices;

	@PostConstruct
	public void createPartControl(Composite parent) {

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		parent.setLayout(gl);

		createControls(parent);

		createTable(parent);
	}

	private void createControls(Composite parent) {

		add = new Button(parent, SWT.PUSH);
		add.setText("Add Workspace");
		GridData addData = new GridData();
		add.setLayoutData(addData);
		add.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				switch (e.type) {
					case SWT.Selection:

						DirectoryDialog dialog = new DirectoryDialog(parent.getShell(), SWT.OPEN | SWT.SINGLE);
						String selectedPath = dialog.open();
						if(selectedPath != null) {
							onSelection(selectedPath);
						}

						break;
				}
			}

			private void onSelection(final String selectedPath) {

				UIJob job = new UIJob("Adding workspace") {

					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {

						try {
							URI location = new URI(selectedPath);
							workspacesServices.addWorkspace(location);

							// Update table UI
							tableViewer.setInput(workspacesServices.getWorkspaces());
							tableViewer.refresh();
							System.err.println("Set workspaces to " + workspacesServices.getWorkspaces());

							return Status.OK_STATUS;
						} catch (ErhenaException | URISyntaxException use) {

							return new Status(IStatus.ERROR, Activator.PLUGIN_ID, use.getMessage(), use);
						}
					}
				};
				job.schedule();
			}
		});

		del = new Button(parent, SWT.PUSH);
		del.setText("Remove Selected");
		GridData delData = new GridData();
		del.setLayoutData(delData);
		del.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				Table table = tableViewer.getTable();
				int[] selected = table.getSelectionIndices();
				List<URI> remove = new ArrayList<URI>();
				for (int i = 0; i < selected.length; i++) {

					remove.add(workspacesServices.getWorkspaces().get(selected[i]));
				}

				UIJob job = new UIJob("Remove workspace") {

					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {

						try {
							for (URI r : remove) {
								workspacesServices.removeWorkspace(r);
							}

							tableViewer.setInput(workspacesServices.getWorkspaces());
							tableViewer.refresh();
							return Status.OK_STATUS;
						} catch (ErhenaException ee) {

							return new Status(IStatus.ERROR, Activator.PLUGIN_ID, ee.getMessage(), ee);
						}
					}
				};
				job.schedule();
			}
		});
	}

	private void createTable(Composite parent) {

		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER);

		// create columns
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText("Location");
		column.setWidth(100);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				URI uri = (URI) element;
				return uri.toString();
			}
		});

		/**
		 * Further config
		 */
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		GridData tableData = new GridData(GridData.FILL_BOTH);
		tableData.horizontalSpan = 2;
		table.setLayoutData(tableData);

		// Configure table, arrayContentProvider handles collections too..
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(workspacesServices.getWorkspaces());
	}

	@Focus
	public void setFocus() {

		tableViewer.getTable().setFocus();
	}

	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {

	}

	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

	}
}
