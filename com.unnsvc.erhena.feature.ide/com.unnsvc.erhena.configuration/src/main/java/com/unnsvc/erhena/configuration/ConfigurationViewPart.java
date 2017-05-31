
package com.unnsvc.erhena.configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import com.unnsvc.erhena.common.services.IConfigurationService;
import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.repository.ERepositoryType;

public class ConfigurationViewPart {

	@Inject
	private IConfigurationService configService;

	@PostConstruct
	public void createPartControl(Composite parent) {

		IRepositoryConfiguration repoConfig = configService.getConfig().getRepositoryConfiguration();

		parent.setLayout(new FillLayout());
		TabFolder tabFolder = new TabFolder(parent, SWT.BORDER | SWT.BOTTOM);

		ConfigurationViewTab workspacesTab = new ConfigurationViewTab(tabFolder, ERepositoryType.WORKSPACE);
		workspacesTab.getViewer().setInput(repoConfig.getWorkspaceRepositories());

		ConfigurationViewTab cachesTab = new ConfigurationViewTab(tabFolder, ERepositoryType.CACHE);
		cachesTab.getViewer().setInput(repoConfig.getCacheRepository());

		ConfigurationViewTab remotesTab = new ConfigurationViewTab(tabFolder, ERepositoryType.REMOTE);
		remotesTab.getViewer().setInput(repoConfig.getRemoteRepositories());

	}
}
