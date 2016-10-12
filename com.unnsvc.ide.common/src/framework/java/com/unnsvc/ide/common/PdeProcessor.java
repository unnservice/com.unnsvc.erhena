
package com.unnsvc.ide.common;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProjectConfiguration;

public class PdeProcessor implements IProcessor {

	@Override
	public void configure(Document configuration, ExecutionType type) {

	}

	@Override
	public void process(IRhenaModule model, IProjectConfiguration configurator) throws RhenaException {

	}

}
