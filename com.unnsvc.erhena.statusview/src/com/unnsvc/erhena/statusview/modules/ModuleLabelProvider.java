
package com.unnsvc.erhena.statusview.modules;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * @TODO Implement font provider to show bold on notifications or color red on
 *       errors if not selected. Once selected revert to standard.
 * 
 * @author noname
 *
 */
public class ModuleLabelProvider implements ITableLabelProvider, IColorProvider {

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

	@Override
	public Color getForeground(Object element) {

		Display display = Display.getCurrent();
		return new Color(display, 0, 0, 0);
	}

	@Override
	public Color getBackground(Object element) {

		Display display = Display.getCurrent();
		return new Color(display, 255, 255, 255);
	}

}
