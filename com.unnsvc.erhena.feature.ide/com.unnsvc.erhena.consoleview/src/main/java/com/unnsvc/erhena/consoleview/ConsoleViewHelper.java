
package com.unnsvc.erhena.consoleview;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class ConsoleViewHelper {

	private MessageConsole findConsole(String name) {

		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}
}


//MessageConsole myConsole = findConsole(CONSOLE_NAME);
//MessageConsoleStream out = myConsole.newMessageStream();
//out.println("Hello from Generic console sample action");
//
//to revewal the console:
//	  IConsole myConsole = ...;// your console instance
//	  IWorkbenchPage page = ...;// obtain the active page
//	  String id = IConsoleConstants.ID_CONSOLE_VIEW;
//	  IConsoleView view = (IConsoleView) page.showView(id);
//	  view.display(myConsole);
//	  