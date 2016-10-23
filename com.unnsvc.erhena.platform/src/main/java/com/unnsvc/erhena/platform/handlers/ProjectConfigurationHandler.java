
package com.unnsvc.erhena.platform.handlers;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IResource;
import com.unnsvc.rhena.core.events.WorkspaceConfigurationEvent;

public class ProjectConfigurationHandler implements IContextListener<WorkspaceConfigurationEvent> {

	private IRhenaContext context;

	public ProjectConfigurationHandler(IRhenaContext context) {

		this.context = context;
	}

	/**
	 * <pre>
	 * 	get eclipse project from ModuleIdentifier
	 *  configure its classpath using resources here
	 * </pre>
	 */
	@Override
	public void onEvent(WorkspaceConfigurationEvent event) throws RhenaException {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		
		IExecutionContext ec = event.getExecutionContext();
		for (EExecutionType eet : EExecutionType.values()) {
			for (IResource resource : event.getExecutionContext().getResources(eet)) {

				
			}
		}
	}

	@Override
	public Class<WorkspaceConfigurationEvent> getType() {

		return WorkspaceConfigurationEvent.class;
	}
}
