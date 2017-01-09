
package com.unnsvc.erhena.statusview.log;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.unnsvc.erhena.statusview.modules.AbstractModuleEntry;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.core.events.LogEvent;

public class LoggingViewTable {

	private TableViewer tableViewer;
	private LogViewFilter viewFilter;
	private LogContentProvider contentProvider;

	public void clear() {

		contentProvider.clear();
//		tableViewer.setInput(null);
	}

	public LoggingViewTable(Composite parent) {

		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		viewFilter = new LogViewFilter(ELogLevel.INFO);
		tableViewer.setFilters(viewFilter);
		contentProvider = new LogContentProvider();

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
		tableViewer.setInput(contentProvider.getLogEvents());
	}

	// public void refresh() {
	//
	// tableViewer.refresh();
	// }

	public TableViewer getTableViewer() {

		return tableViewer;
	}

	public void setFilter(ELogLevel level) {

		this.viewFilter.setLevel(level);
		this.tableViewer.refresh();
	}

	public void setFilter(AbstractModuleEntry entry) {

		this.viewFilter.setType(entry);
		this.tableViewer.refresh();
	}

	public void addLogEvent(LogEvent logEvent) {

		contentProvider.addElement(logEvent);
		tableViewer.refresh();
	}

	public void refresh() {
		
		tableViewer.refresh();
	}
}
