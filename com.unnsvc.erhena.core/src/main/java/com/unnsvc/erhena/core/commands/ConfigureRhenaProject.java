
package com.unnsvc.erhena.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.unnsvc.erhena.core.nature.RhenaNature;

public class ConfigureRhenaProject extends AbstractHandler {

	private ISelection selection;

	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
				}
				if (project != null) {
					try {
						if (project.hasNature(RhenaNature.NATURE_ID)) {
							unconfigureNature(project);
						} else {
							configureNature(project);
						}
					} catch (CoreException e) {
						// TODO log something
						throw new ExecutionException("Failed to toggle nature", e);
					}
				}
			}
		}

		return null;
	}

	private void unconfigureNature(IProject project) throws CoreException {

		IProjectDescription desc = project.getDescription();
		// mutable list
		List<String> natures = new ArrayList<String>(Arrays.asList(desc.getNatureIds()));
		natures.remove(RhenaNature.NATURE_ID);
		desc.setNatureIds(natures.toArray(new String[natures.size()]));
		project.setDescription(desc, null);
	}

	/**
	 * Toggles sample nature on a project
	 *
	 * @param project
	 *            to have sample nature added or removed
	 */
	private void configureNature(IProject project) throws CoreException {

		IProjectDescription desc = project.getDescription();
		// mutable list
		List<String> natures = new ArrayList<String>(Arrays.asList(desc.getNatureIds()));
		natures.add(RhenaNature.NATURE_ID);
		desc.setNatureIds(natures.toArray(new String[natures.size()]));
		project.setDescription(desc, null);
	}

}
