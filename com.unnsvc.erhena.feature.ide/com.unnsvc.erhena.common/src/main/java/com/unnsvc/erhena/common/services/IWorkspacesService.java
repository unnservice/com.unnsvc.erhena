
package com.unnsvc.erhena.common.services;

import java.net.URI;
import java.util.List;

import com.unnsvc.erhena.common.exceptions.ErhenaException;

/**
 * The workspaces services provides workspace tracking and persistence
 * capabilities
 * 
 * @author noname
 *
 */
public interface IWorkspacesService {

	public List<URI> getWorkspaces();

	public void addWorkspace(URI location) throws ErhenaException;

	public void removeWorkspace(URI location) throws ErhenaException;

}
