
package com.unnsvc.erhena.statusview.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModuleViewContentProvider implements IStructuredContentProvider {

	private List<ModuleEntry> activeModules;

	public ModuleViewContentProvider() {

		this.activeModules = new ArrayList<ModuleEntry>();
		this.activeModules.add(new AllEntry());
		this.activeModules.add(new CoreEntry());
	}

	public void addElement(ModuleIdentifier moduleIdentifier) {

		activeModules.add(new ModuleEntry(moduleIdentifier));
	}

	@Override
	public Object[] getElements(Object inputElement) {

		return activeModules.toArray();
	}

	public List<ModuleEntry> getActiveModules() {

		return activeModules;
	}

	public void removeElement(ModuleIdentifier identifier) {

		Iterator<ModuleEntry> iter = activeModules.iterator();
		while (iter.hasNext()) {

			ModuleEntry entry = iter.next();
			if (entry.getIdentifier().equals(identifier)) {
				iter.remove();
			}
		}
	}
}
