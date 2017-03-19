
package com.unnsvc.erhena.itests.tests;

import org.junit.Before;

import com.unnsvc.erhena.common.InjectionHelper;
import com.unnsvc.erhena.itests.Activator;

public abstract class AbstractTest {

	@Before
	public void before() {

		InjectionHelper.inject(Activator.class, this);
	}
}
