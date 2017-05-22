
package com.unnsvc.erhena.workspaces.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.unnsvc.erhena.common.exceptions.ErhenaException;
import com.unnsvc.erhena.common.services.IWorkspacesService;
import com.unnsvc.erhena.workspaces.Activator;
import com.unnsvc.erhena.workspaces.Constants;

@Component(service = IWorkspacesService.class, immediate = true)
public class WorkspacesService implements IWorkspacesService {

	private List<URI> workspaces;

	/**
	 * As normal OSGi services probably don't have @PostConstruct, we have to do
	 * the loading in constructor instead of in @PostConstruct
	 * 
	 * @throws ErhenaException
	 */
	public WorkspacesService() throws ErhenaException {

		workspaces = new ArrayList<URI>();
		loadPreferences();
	}

	private void loadPreferences() throws ErhenaException {

		Preferences prefs = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		String workspacesStr = prefs.get(Constants.PROP_WORKSPACES, null);
		if (workspacesStr != null) {
			try {
				for (String workspaceStr : workspacesStr.split(";")) {
					workspaces.add(new URI(workspaceStr));
				}
			} catch (URISyntaxException use) {
				throw new ErhenaException(use);
			}
		}
	}

	private void savePreferences() throws ErhenaException {

		if (!workspaces.isEmpty()) {
			try {
				StringBuilder sb = new StringBuilder();
				for (URI workspace : workspaces) {
					sb.append(workspace.toString()).append(";");
				}
				String workspacesStr = sb.toString().substring(0, sb.toString().length() - 1);

				Preferences prefs = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
				prefs.put(Constants.PROP_WORKSPACES, workspacesStr);

				prefs.flush();
			} catch (BackingStoreException ioe) {

				throw new ErhenaException(ioe);
			}
		}
	}

	@Override
	public List<URI> getWorkspaces() {

		return workspaces;
	}

	@Override
	public void addWorkspace(URI location) throws ErhenaException {

		if (!workspaces.contains(location)) {
			workspaces.add(location);
			savePreferences();
		}
	}

	@Override
	public void removeWorkspace(URI location) throws ErhenaException {

		workspaces.remove(location);
		savePreferences();
	}
}
