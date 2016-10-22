
package com.unnsvc.erhena.statusview.modules;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent.EAddRemove;
import com.unnsvc.rhena.core.logging.LogEvent;

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

	public void onModule(ModuleAddRemoveEvent moduleAddRemove) {

		if (moduleAddRemove.getAddRemove() == EAddRemove.ADDED) {
			ModuleEntry moduleEntry = new ModuleEntry(moduleAddRemove.getIdentifier());
			moduleEntry.setActivity(true);
			moduleViewContentProvider.addElement(moduleEntry);
		} else if (moduleAddRemove.getAddRemove() == EAddRemove.REMOVED) {
			moduleViewContentProvider.removeElement(moduleAddRemove.getIdentifier());
		}

		refresh();
	}

	public void onLogEvent(LogEvent logEvent) {

		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		AbstractModuleEntry entry = (AbstractModuleEntry) selection.getFirstElement();

		for (AbstractModuleEntry moduleEntry : moduleViewContentProvider.getActiveModules()) {

			if (moduleEntry != entry) {
				configureUnselectedEntry(moduleEntry, logEvent);
			}
		}

		refresh();
	}

	private void configureUnselectedEntry(AbstractModuleEntry moduleEntry, LogEvent logEvent) {
		
		moduleEntry.setActivity(true);
		moduleEntry.setLevel(logEvent.getLevel());
	}
}
