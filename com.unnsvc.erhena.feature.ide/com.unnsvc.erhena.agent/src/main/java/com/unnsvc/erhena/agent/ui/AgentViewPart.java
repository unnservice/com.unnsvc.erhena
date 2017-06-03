
package com.unnsvc.erhena.agent.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.unnsvc.erhena.common.services.IAgentService;

public class AgentViewPart {

	@Inject
	private IAgentService agentService;

	@PostConstruct
	public void createControl(Composite parent) {

		GridLayout layout = new GridLayout();
		parent.setLayout(layout);

		Text text = new Text(parent, SWT.SINGLE);
	}
}
