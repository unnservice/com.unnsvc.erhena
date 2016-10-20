
package com.unnsvc.erhena.statusview.log;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.unnsvc.erhena.statusview.LogViewFilter;
import com.unnsvc.erhena.statusview.modules.ModuleEntry;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.logging.LogEvent;

public class LoggingViewTable {

	private TableViewer tableViewer;
	private LogViewFilter viewFilter;

	public LoggingViewTable(Composite parent, LogContentProvider contentProvider) {

		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		viewFilter = new LogViewFilter(ELogLevel.INFO);
		tableViewer.setFilters(viewFilter);

		TableViewerColumn levelColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		levelColumn.getColumn().setWidth(100);
		levelColumn.getColumn().setText("Level");
		levelColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				return ((LogEvent) element).getLevel().toString().toLowerCase();
			}
		});
		TableViewerColumn messageColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		messageColumn.getColumn().setWidth(300);
		messageColumn.getColumn().setText("Message");
		messageColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {

				return ((LogEvent) element).getMessage();
			}
		});

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer.setContentProvider(contentProvider);
	}

	public void refresh() {

		tableViewer.refresh();
	}

	public TableViewer getTableViewer() {

		return tableViewer;
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

	public void setFilter(ELogLevel level) {

		this.viewFilter.setLevel(level);
		this.tableViewer.refresh();
	}

	public void setFilter(ModuleEntry entry) {

		this.viewFilter.setType(entry);
		this.tableViewer.refresh();
	}
}
