package com.unnsvc.erhena.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;

public class ErhenaUtils {

	public static void createFolder(IFolder folder) throws CoreException {

		IContainer parent = folder.getParent();
		if (parent instanceof IFolder) {
			createFolder((IFolder) parent);
		}
		if (!folder.exists()) {
			folder.create(false, true, null);
		}
	}
	
	public static String locateClasspath(String bundleName) throws MalformedURLException, URISyntaxException, IOException {

		URI fileUri = FileLocator.resolve(new URL("platform:/plugin/" + bundleName + "/META-INF/" + bundleName)).toURI();
		File file = new File(fileUri);
		String fileStr = file.getAbsolutePath();

		String path = fileStr.substring(0, fileStr.length() - ("/META-INF/" + bundleName).length()) + "/";
		return path;
	}
}
