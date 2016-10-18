
package com.unnsvc.erhena.statusview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class LoggingViewTable {

	private Table table;

	public LoggingViewTable(Composite composite) {

		composite.setLayout(new FillLayout());

		table = new Table(composite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// int fontWidth = getFontWidth(table.getDisplay().getSystemFont());

		TableColumn time = new TableColumn(table, SWT.NONE);
		time.setText("TIME");
		// time.setWidth(fontWidth * 10);
		time.setResizable(false);

		TableColumn level = new TableColumn(table, SWT.NONE);
		level.setText("LEVEL");
		// level.setWidth(fontWidth * 10);
		level.setResizable(false);

		TableColumn logger = new TableColumn(table, SWT.NONE);
		logger.setText("LOGGER");
		// logger.setWidth(fontWidth * 40);
		logger.setResizable(false);

		TableColumn message = new TableColumn(table, SWT.NONE);
		message.setText("MESSAGE");

		// int count = 128;
		// for (int i = 0; i < count; i++) {
		// TableItem item = new TableItem(table, SWT.NONE);
		// item.setText(0, "x");
		// item.setText(1, "y");
		// item.setText(2, "!");
		// item.setText(3, "this stuff behaves the way I expect");
		// }
		// for (int i = 0; i < table.getColumns().length; i++) {
		// table.getColumn(i).pack();
		// }
		table.setSize(table.computeSize(SWT.DEFAULT, 200));
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

	public void onLogEvent(ILoggingEvent logEvent) {
		
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, logEvent.getTimeStamp() + "");
		item.setText(1, logEvent.getLevel().toString());
		item.setText(2, logEvent.getLoggerName());
		item.setText(3, logEvent.getMessage());

		for (int i = 0; i < table.getColumns().length; i++) {
			table.getColumn(i).pack();
		}

		table.setSelection(item);
	}

}
