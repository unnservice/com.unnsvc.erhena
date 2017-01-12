
package com.unnsvc.erhena.events;

public class AgentProcessStartExitEvent {

	public static final String TOPIC = "com/unnsvc/erhena/agentstartexit";
	private EStartStop startStop;

	public AgentProcessStartExitEvent(EStartStop startStop) {

		this.startStop = startStop;
	}

	public EStartStop getStartStop() {

		return startStop;
	}

	public enum EStartStop {

		START, STOP;
	}
}
