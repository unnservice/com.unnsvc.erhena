
package com.unnsvc.erhena.statusview.modules;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class ModuleViewTable {

	private TableViewer tableViewer;
	private ModuleLabelProvider labelProvider;

	public ModuleViewTable(Composite composite, ModuleViewContentProvider contentProvider) {

		this.labelProvider = new ModuleLabelProvider();

		tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);

		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setContentProvider(contentProvider);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
	}

	public void refresh() {

		tableViewer.refresh();
	}
	

	public TableViewer getTableViewer() {

		return tableViewer;
	}
}
