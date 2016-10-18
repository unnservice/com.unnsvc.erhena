
package com.unnsvc.erhena.statusview.modules;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class ModuleLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {

		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {

		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		ModuleEntry entry = (ModuleEntry) element;
		if (entry instanceof AllEntry) {

			return "all";
		} else if (entry instanceof CoreEntry) {

			return "core";
		} else {

			return entry.getIdentifier().toString();
		}
	}

}
