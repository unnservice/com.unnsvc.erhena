
package com.unnsvc.erhena.repositories;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.repository.ERepositoryType;

public class RepositoriesViewPart {

	@Inject
	private IConfigurationService configService;

	@PostConstruct
	public void createPartControl(Composite parent) throws RhenaException {
		
		configService.loadConfiguration();

//		IRepositoryConfiguration repoConfig = configService.getConfig().getRepositoryConfiguration();
		IRhenaConfiguration config = configService.getConfig();

		parent.setLayout(new FillLayout());
		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER | SWT.BOTTOM);

		RepositoriesViewTab workspacesTab = new RepositoriesViewTab(config, tabFolder, ERepositoryType.WORKSPACE) {

			@Override
			public void onPersistRepositories() throws ErhenaException {

				configService.persistRepositories();
			}
		};
		workspacesTab.updateTable();

		RepositoriesViewTab cachesTab = new RepositoriesViewTab(config, tabFolder, ERepositoryType.CACHE) {

			@Override
			public void onPersistRepositories() throws ErhenaException {

				configService.persistRepositories();
			}
		};
		cachesTab.updateTable();

		RepositoriesViewTab remotesTab = new RepositoriesViewTab(config, tabFolder, ERepositoryType.REMOTE) {

			@Override
			public void onPersistRepositories() throws ErhenaException {

				configService.persistRepositories();
			}
		};
		remotesTab.updateTable();

	}
}
