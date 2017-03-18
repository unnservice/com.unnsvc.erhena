package com.unnsvc.erhena.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

public interface IRhenaProject {

	public IProject getProject();

	public IJavaProject getJavaProject();
}
