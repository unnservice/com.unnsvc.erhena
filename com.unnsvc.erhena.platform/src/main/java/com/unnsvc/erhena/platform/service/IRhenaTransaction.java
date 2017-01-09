
package com.unnsvc.erhena.platform.service;

import com.unnsvc.rhena.common.IRhenaEngine;

public interface IRhenaTransaction {

	public void execute(IRhenaEngine engine) throws Throwable;

}
