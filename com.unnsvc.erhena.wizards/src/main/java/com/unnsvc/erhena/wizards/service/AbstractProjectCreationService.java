
package com.unnsvc.erhena.wizards.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import com.unnsvc.erhena.wizards.Activator;

public abstract class AbstractProjectCreationService {

	protected InputStream getModuleTemplate(String componentName, String projectName) throws CoreException {

		URL moduleTemplate = Activator.class.getResource("/templates/module.xml.tpl");
		try (InputStream is = moduleTemplate.openStream()) {

			StringBuilder sb = new StringBuilder();
			int buff = -1;
			while ((buff = is.read()) != -1) {
				sb.append((char) buff);
			}
			String template = sb.toString().replaceAll(Pattern.quote("##COMPONENTID##"), componentName);
			return new ByteArrayInputStream(template.getBytes());
		} catch (IOException ioe) {
			throw new CoreException(new Status(Status.ERROR, Activator.PLUGIN_ID, ioe.getMessage(), ioe));
		}
	}
}
