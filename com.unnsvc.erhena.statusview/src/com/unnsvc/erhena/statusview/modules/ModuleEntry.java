
package com.unnsvc.erhena.statusview.modules;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModuleEntry extends AbstractModuleEntry {

	private ModuleIdentifier identifier;
	// @TODO have error states

	public ModuleEntry(ModuleIdentifier identifier) {

		this.identifier = identifier;
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}
}
