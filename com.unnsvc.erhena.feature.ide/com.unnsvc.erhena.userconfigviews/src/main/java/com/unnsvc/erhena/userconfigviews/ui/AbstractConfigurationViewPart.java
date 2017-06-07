
package com.unnsvc.erhena.userconfigviews.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;

public abstract class AbstractConfigurationViewPart {

	protected TableViewer viewer;
	protected Button add;
	protected Button del;
	protected Button up;
	protected Button down;
	protected ERepositoryType repoType;

	public AbstractConfigurationViewPart(TabFolder tabFolder, ERepositoryType repoType) {

		TabItem item = new TabItem(tabFolder, SWT.NONE);
		item.setText(repoType.toString());
		this.repoType = repoType;

		Composite container = new Composite(tabFolder, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		item.setControl(container);

		createControls(container);
		createTableViewer(container);
		createUiLogic(container);
	}

	public abstract void createUiLogic(Composite parent);

	private void createControls(Composite container) {

		viewer = new TableViewer(container);
		viewer.setContentProvider(new ArrayContentProvider());

		TableViewerColumn protocolColumn = new TableViewerColumn(viewer, SWT.NONE);
		protocolColumn.getColumn().setWidth(100);
		protocolColumn.getColumn().setText("Protocol");
		protocolColumn.setLabelProvider(new ColumnLabelProvider() {
			
			@Override
			public String getText(Object element) {
			
				
				IRepositoryDefinition repoDef = (IRepositoryDefinition) element;
				return repoDef.getLocation().getScheme();
			}
		});
		
		TableViewerColumn locationColumn = new TableViewerColumn(viewer, SWT.NONE);
		locationColumn.getColumn().setWidth(100);
		locationColumn.getColumn().setText("Location");
		locationColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				IRepositoryDefinition repoDef = (IRepositoryDefinition) element;
				return repoDef.getLocation().toString();
			}
		});
		

		// configure table

		GridData data = new GridData(GridData.FILL_BOTH);
		viewer.getTable().setLayoutData(data);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
	}

	private void createTableViewer(Composite container) {

		Composite buttons = new Composite(container, SWT.NONE);
		GridData buttonsData = new GridData(GridData.FILL_VERTICAL);
		buttons.setLayoutData(buttonsData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		buttons.setLayout(layout);

		add = new Button(buttons, SWT.PUSH);
		add.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		add.setText("Add");

		del = new Button(buttons, SWT.PUSH);
		del.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		del.setText("Remove");

		up = new Button(buttons, SWT.PUSH);
		up.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		up.setText("Up");

		down = new Button(buttons, SWT.PUSH);
		down.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		down.setText("Down");
	}

	public TableViewer getViewer() {

		return viewer;
	}
	
	public abstract void onPersistRepositories() throws ErhenaException;
}
