
package com.unnsvc.erhena.userconfigviews.ui;

import java.io.File;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.progress.UIJob;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.userconfigviews.Activator;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.config.RepositoryDefinition;

public abstract class RepositoriesViewTab extends AbstractConfigurationViewPart {

	private IRhenaConfiguration config;

	public RepositoriesViewTab(IRhenaConfiguration config, TabFolder tabFolder, ERepositoryType repoType) {

		super(tabFolder, repoType);
		this.config = config;
	}

	@Override
	public void createUiLogic(Composite parent) {

		add.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {

				if (e.type == SWT.Selection) {

					DirectoryDialog dialog = new DirectoryDialog(parent.getShell(), SWT.OPEN | SWT.SINGLE);
					String selectedPath = dialog.open();

					if (selectedPath != null) {
						UIJob job = new UIJob("Adding a repository") {

							@Override
							public IStatus runInUIThread(IProgressMonitor monitor) {

								try {

									onLocationSelection(selectedPath);
									onPersistRepositories();

									return Status.OK_STATUS;
								} catch (ErhenaException ee) {

									return new Status(IStatus.ERROR, Activator.PLUGIN_ID, ee.getMessage(), ee);
								}
							}
						};
						job.schedule();
					}
				}
			}
		});

		del.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				if (event.type == SWT.Selection) {

					new UIJob("Remove repository") {

						@SuppressWarnings("unchecked")
						@Override
						public IStatus runInUIThread(IProgressMonitor monitor) {

							try {
								IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
								selection.iterator().forEachRemaining(tableItemObject -> {

									RepositoryDefinition def = (RepositoryDefinition) tableItemObject;

									switch (repoType) {
										case CACHE:
											config.getRepositoryConfiguration().getCacheRepositories().remove(def);
											break;
										case REMOTE:
											config.getRepositoryConfiguration().getRemoteRepositories().remove(def);
											break;
										case WORKSPACE:
											config.getRepositoryConfiguration().getWorkspaceRepositories().remove(def);
											break;
									}
								});

								updateTable();

								onPersistRepositories();
								
								return Status.OK_STATUS;
							} catch (ErhenaException ee) {
								return new Status(IStatus.ERROR, Activator.PLUGIN_ID, ee.getMessage(), ee);
							}
						}
					}.schedule();
				}
			}
		});
	}

	public void onLocationSelection(String selectedPath) throws ErhenaException {

		File location = new File(selectedPath);
		URI locationUri = location.toURI();

		RepositoryIdentifier identifier = new RepositoryIdentifier(location.toString());
		RepositoryDefinition def = new RepositoryDefinition(repoType, identifier, locationUri);

		switch (repoType) {
			case CACHE:
				config.getRepositoryConfiguration().addCacheRepository(def);
				break;
			case REMOTE:
				config.getRepositoryConfiguration().addRemoteRepository(def);
				break;
			case WORKSPACE:
				config.getRepositoryConfiguration().addWorkspaceRepositories(def);
				break;
		}

		updateTable();
	}

	public void updateTable() {

		switch (repoType) {
			case CACHE:
				viewer.setInput(config.getRepositoryConfiguration().getCacheRepositories());
				break;
			case WORKSPACE:
				viewer.setInput(config.getRepositoryConfiguration().getWorkspaceRepositories());
				break;
			case REMOTE:
				viewer.setInput(config.getRepositoryConfiguration().getRemoteRepositories());
				break;
		}

	}

}
