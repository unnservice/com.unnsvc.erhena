
package com.unnsvc.erhena.statusview.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModuleViewContentProvider implements IStructuredContentProvider {

	private List<AbstractModuleEntry> activeModules;

	public ModuleViewContentProvider() {

		clear();
	}

	public void clear() {

		this.activeModules = new ArrayList<AbstractModuleEntry>();
		// this.activeModules.add(new AllEntry());
		this.activeModules.add(new CoreEntry());
	}

	public void addElement(ModuleEntry entry) {

		activeModules.add(entry);
	}

	@Override
	public Object[] getElements(Object inputElement) {

		return activeModules.toArray();
	}

	public List<AbstractModuleEntry> getActiveModules() {

		return activeModules;
	}

	public void removeElement(ModuleIdentifier identifier) {

		Iterator<AbstractModuleEntry> iter = activeModules.iterator();
		while (iter.hasNext()) {

			AbstractModuleEntry entry = iter.next();
			if (entry instanceof ModuleEntry) {
				ModuleEntry mo = (ModuleEntry) entry;
				if (mo.getIdentifier().equals(identifier)) {
					iter.remove();
				}
			}
		}
	}

	public boolean containsModule(ModuleIdentifier identifier) {

		for (AbstractModuleEntry entry : activeModules) {
			if (entry instanceof ModuleEntry) {
				ModuleEntry me = (ModuleEntry) entry;
				if (me.getIdentifier().equals(identifier)) {
					return true;
				}
			}
		}
		return false;
	}
}
