
package com.unnsvc.erhena.common.events;

import com.unnsvc.rhena.profiling.report.IDiagnosticReport;

public class ProfilerDiagnosticsEvent {

	public static final String TOPIC = "com/unnsvc/erhena/profilerdiagnostics";
	private IDiagnosticReport report;

	public ProfilerDiagnosticsEvent(IDiagnosticReport report) {

		this.report = report;
	}

	public IDiagnosticReport getReport() {

		return report;
	}
}
