
package com.unnsvc.ide.common;

import java.io.File;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.IProjectConfiguration;

public class PdeGenerator implements IGenerator {

	@Override
	public void configure(Document configuration, ExecutionType type) {

	}

	@Override
	public File generate(IRhenaModule model, IProjectConfiguration configurator) throws RhenaException {

		return null;
	}

}
