
package com.unnsvc.erhena.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import com.unnsvc.rhena.common.repository.ERepositoryType;

public class ConfigurationViewTab extends AbstractConfigurationViewPart {

	public ConfigurationViewTab(TabFolder tabFolder, ERepositoryType repoType) {

		super(tabFolder, repoType);
	}

	@Override
	public void createUiLogic(Composite parent) {

		add.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				if (e.type == SWT.Selection) {

					DirectoryDialog dialog = new DirectoryDialog(parent.getShell(), SWT.OPEN | SWT.SINGLE);
					String selectedPath = dialog.open();

					if (selectedPath != null) {
						onLocationSelection(selectedPath);
					}
				}
			}
		});
	}

	@Override
	public void onLocationSelection(String selectedPath) {

	}
}
