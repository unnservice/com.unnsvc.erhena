package com.unnsvc.erhena.itests;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Before;

import com.unnsvc.erhena.common.InjectionHelper;
import com.unnsvc.erhena.core.builder.RhenaBuilder;

public abstract class AbstractServiceTest {
	
	protected NullProgressMonitor monitor;

	@Before
	public void before() throws Exception {

//		BundleContext bundleContext = FrameworkUtil.getBundle(TestPlatformSerivce.class).getBundleContext();
//		IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
//		ContextInjectionFactory.inject(this, eclipseContext);
		
		monitor = new NullProgressMonitor();
		InjectionHelper.inject(Activator.class, this);
	}
	
	
	
	public void assertBuilderPresent(IProject project, String builderId) throws CoreException {
		
		boolean contains = Arrays.asList(project.getDescription().getBuildSpec()).stream().anyMatch(b -> b.getBuilderName().equals(builderId));
		Assert.assertTrue(contains);
	}

}
