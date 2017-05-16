package com.unnsvc.erhena.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

		String path = locateClasspathUrl(bundleName).getPath();
		return path;
	}
	
	public static URL locateClasspathUrl(String bundleName) throws MalformedURLException, IOException {
		
		URL fileUrl = FileLocator.resolve(new URL("platform:/plugin/" + bundleName + "/META-INF/" + bundleName));
		if(fileUrl.getProtocol().equals("jar")) {

			URL ret = new File(fileUrl.getPath().substring("file:".length(), fileUrl.getPath().lastIndexOf("!"))).toURI().toURL();
			return ret;
		}

		String fileUrlStr = fileUrl.toString();
		return new URL(fileUrlStr.substring(0, fileUrlStr.length() - ("/META-INF/" + bundleName).length()) + "/");
	}
}
