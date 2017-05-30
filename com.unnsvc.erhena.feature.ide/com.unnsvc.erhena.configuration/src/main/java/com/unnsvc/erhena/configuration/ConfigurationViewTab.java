
package com.unnsvc.erhena.configuration;


import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ConfigurationViewTab {

	public ConfigurationViewTab(TabFolder tabFolder, String title) {

		TabItem item = new TabItem(tabFolder, SWT.NONE);
		item.setText(title);
		
		Composite container = new Composite(tabFolder, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		item.setControl(container);
		
		createControls(container);
		createTableViewer(container);
	}

	private void createControls(Composite container) {

		TableViewer viewer = new TableViewer(container);
		
		//  configure table
		
		GridData data = new GridData(GridData.FILL_BOTH);
		viewer.getTable().setLayoutData(data);
	}

	private void createTableViewer(Composite container) {

		Composite buttons = new Composite(container, SWT.NONE);
		GridData buttonsData = new GridData(GridData.FILL_VERTICAL);
		buttons.setLayoutData(buttonsData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		buttons.setLayout(layout);
		
		Button add = new Button(buttons, SWT.PUSH);
		add.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		add.setText("Add");
		
		Button del = new Button(buttons, SWT.PUSH);
		del.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		del.setText("Remove");
		
		Button up = new Button(buttons, SWT.PUSH);
		up.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		up.setText("Up");
		
		Button down = new Button(buttons, SWT.PUSH);
		down.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		down.setText("Down");
	}
}
