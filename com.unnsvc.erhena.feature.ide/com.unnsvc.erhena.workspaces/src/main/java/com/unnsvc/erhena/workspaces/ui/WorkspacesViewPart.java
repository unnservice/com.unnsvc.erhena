
package com.unnsvc.erhena.workspaces.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class WorkspacesViewPart {

	private Table table;

	@PostConstruct
	public void createPartControl(Composite parent) {

		table = new Table(parent, SWT.VIRTUAL | SWT.BORDER);
	}

	@Focus
	public void setFocus() {

		table.setFocus();
	}

	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object o) {

	}

	@Inject
	@Optional
	public void setSelection(@Named(IServiceConstants.ACTIVE_SELECTION) Object[] selectedObjects) {

	}
}
