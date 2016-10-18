package com.unnsvc.erhena.statusview.log;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.unnsvc.rhena.core.logging.LogEvent;

public class LogLabelProvider implements ITableLabelProvider {

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

		LogEvent evt = (LogEvent) element;
		
		switch(columnIndex) {
			
			case 0:
				return evt.getMessage();
		}
		
		return "empty";
	}

}
