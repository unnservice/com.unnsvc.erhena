
package com.unnsvc.erhena.core.builder;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

import com.unnsvc.erhena.common.InjectionHelper;
import com.unnsvc.erhena.core.Activator;
import com.unnsvc.erhena.core.registry.IChangeRegistryService;

public class ModuleResourceChangeListener implements IResourceChangeListener {

	@Inject
	private IChangeRegistryService changeRegistry;

	public ModuleResourceChangeListener() {

		InjectionHelper.inject(Activator.class, this);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		try {
			DeltaCollector coll = new DeltaCollector(event);
			for (IProject project : coll.getProjectpaths().keySet()) {
				changeRegistry.addChanges(project, coll.getProjectpaths().get(project));
			}
		} catch (CoreException e) {

			e.printStackTrace();
		}

	}
}
