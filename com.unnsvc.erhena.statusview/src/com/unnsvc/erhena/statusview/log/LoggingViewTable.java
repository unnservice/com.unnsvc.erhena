
package com.unnsvc.erhena.statusview.log;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class LoggingViewTable {

	private TableViewer tableViewer;
	private LogLabelProvider labelProvider;

	public LoggingViewTable(Composite composite, LogContentProvider contentProvider) {

		labelProvider = new LogLabelProvider();

		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.NONE | SWT.FULL_SELECTION);

		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setContentProvider(contentProvider);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}

	public void refresh() {

		tableViewer.refresh();
	}

	private int getFontWidth(Font font) {

		GC gc = null;
		try {
			gc = new GC(font.getDevice());
			FontMetrics fm = gc.getFontMetrics();
			return fm.getAverageCharWidth();
		} finally {
			if (gc != null) {
				gc.dispose();
			}
		}
	}

	public TableViewer getTableViewer() {

		return tableViewer;
	}

}
