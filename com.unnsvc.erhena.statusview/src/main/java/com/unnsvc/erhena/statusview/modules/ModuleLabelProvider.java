
package com.unnsvc.erhena.statusview.modules;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.unnsvc.rhena.common.logging.ELogLevel;

/**
 * @TODO Implement font provider to show bold on notifications or color red on
 *       errors if not selected. Once selected revert to standard.
 * 
 * @author noname
 *
 */
public class ModuleLabelProvider implements ITableLabelProvider, IColorProvider, IFontProvider {

	private Color black;
	private Color orange;
	private Color red;
	private Color white;

	public ModuleLabelProvider() {

		Display display = Display.getCurrent();
		black = new Color(display, 0, 0, 0);
		orange = new Color(display, 255, 127, 0);
		red = new Color(display, 255, 0, 0);
		white = new Color(display, 255, 255, 255);
	}

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

		AbstractModuleEntry entry = (AbstractModuleEntry) element;
		// if (entry instanceof AllEntry) {
		//
		// return "all";
		// } else
		if (entry instanceof CoreEntry) {

			return "Rhena Core";
		} else if (entry instanceof ModuleEntry) {
			ModuleEntry mo = (ModuleEntry) entry;
			return mo.getIdentifier().toString();
		} else {
			return "null";
		}
	}

	@Override
	public Color getForeground(Object element) {

		AbstractModuleEntry entry = (AbstractModuleEntry) element;
		if (entry.getLevel().equals(ELogLevel.WARN)) {
			return orange;
		} else if (entry.getLevel().equals(ELogLevel.ERROR)) {
			return red;
		} else {
			return black;
		}
	}

	@Override
	public Color getBackground(Object element) {

		return white;
	}

	@Override
	public Font getFont(Object element) {

		AbstractModuleEntry entry = (AbstractModuleEntry) element;
		Font font = JFaceResources.getFont(JFaceResources.DEFAULT_FONT);

		if (entry.isActivity()) {
			FontDescriptor fd = FontDescriptor.createFrom(font);
			fd = fd.setStyle(SWT.BOLD);
			font = fd.createFont(font.getDevice());
		}

		return font;
	}

}

// private int getFontWidth(Font font) {
//
// GC gc = null;
// try {
// gc = new GC(font.getDevice());
// FontMetrics fm = gc.getFontMetrics();
// return fm.getAverageCharWidth();
// } finally {
// if (gc != null) {
// gc.dispose();
// }
// }
// }

// font1 = JFaceResources.getFont(JFaceResources.BANNER_FONT);
// font2 = JFaceResources.getFont(JFaceResources.HEADER_FONT);