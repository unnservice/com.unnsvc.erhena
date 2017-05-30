
package com.unnsvc.erhena.configuration;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

public class ConfigurationViewPart {

	@PostConstruct
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());
		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER | SWT.BOTTOM);

		ConfigurationViewTab workspacesTab = new ConfigurationViewTab(tabFolder, "workspaces");

		ConfigurationViewTab cachesTab = new ConfigurationViewTab(tabFolder, "caches");

		ConfigurationViewTab remotesTab = new ConfigurationViewTab(tabFolder, "remotes");

	}
}
