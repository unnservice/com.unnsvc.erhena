
package com.unnsvc.erhena.statusview.modules;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.unnsvc.rhena.core.events.LogEvent;

public class ModuleViewTable {

	private TableViewer tableViewer;
	private ModuleLabelProvider labelProvider;
	private ModuleViewContentProvider moduleViewContentProvider;

	public ModuleViewTable(Composite composite) {

		this.labelProvider = new ModuleLabelProvider();
		this.moduleViewContentProvider = new ModuleViewContentProvider();

		tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);

		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setContentProvider(moduleViewContentProvider);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);

		tableViewer.setInput(moduleViewContentProvider.getActiveModules());

		table.select(0);
	}

	public void refresh() {

		tableViewer.refresh();
	}

	public TableViewer getTableViewer() {

		return tableViewer;
	}

	public void onLogEvent(LogEvent logEvent) {

		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		AbstractModuleEntry selected = (AbstractModuleEntry) selection.getFirstElement();
		
		/**
		 * @TODO this is highly inefficient, figure out some faster way later
		 */
		if(logEvent.getIdentifier() != null && !moduleViewContentProvider.containsModule(logEvent.getIdentifier())) {
			
			ModuleEntry moduleEntry = new ModuleEntry(logEvent.getIdentifier());
			moduleEntry.setActivity(true);
			moduleViewContentProvider.addElement(moduleEntry);
		}

		/**
		 * We only want to configure unselected entries so they "light up" in
		 * colors representing the log level or black for normal activity
		 */
		for (AbstractModuleEntry moduleEntry : moduleViewContentProvider.getActiveModules()) {

			if (moduleEntry != selected) {

				// Only select entries that aren't selected in the UI, plus is
				// relevant to the log event

				if (moduleEntry instanceof CoreEntry && logEvent.getIdentifier() == null) {
					configureUnselectedEntry(moduleEntry, logEvent);
					break;
				}

				if (moduleEntry instanceof ModuleEntry && logEvent.getIdentifier() != null) {
					ModuleEntry me = (ModuleEntry) moduleEntry;
					if (me.getIdentifier().equals(logEvent.getIdentifier())) {
						configureUnselectedEntry(moduleEntry, logEvent);
						break;
					}
				}
			}
		}

		refresh();
	}

	private void configureUnselectedEntry(AbstractModuleEntry moduleEntry, LogEvent logEvent) {

		moduleEntry.setActivity(true);
		moduleEntry.setLevel(logEvent.getLevel());
	}
}
