package com.lithium.streams.compliance.handler;

import com.lithium.streams.compliance.api.ComplianceBatchHandler;
import com.lithium.streams.compliance.api.ComplianceEvent;

public class HeaderPopulateHandler implements ComplianceBatchHandler {

	private ComplianceBatchHandler next;

	@Override
	public void setNext(ComplianceBatchHandler handler) {
		this.next = handler;
	}

	@Override
	public void handleRequest(ComplianceEvent event) {
		System.out.println("Handle Request: HeaderPopulateHandler: " + event.getEvent());
	}

	@Override
	public ComplianceBatchHandler getNext() {
		return next;
	}

	@Override
	public String toString() {
		return "HeaderPopulateHandler [next=" + next + "]";
	}

}
