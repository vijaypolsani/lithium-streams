package com.lithium.streams.compliance.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.api.ComplianceBatchHandler;
import com.lithium.streams.compliance.api.ComplianceEvent;

public class ClientTrackingHandler implements ComplianceBatchHandler {
	private static final Logger log = LoggerFactory.getLogger(ClientTrackingHandler.class);
	private ComplianceBatchHandler next;

	@Override
	public void setNext(ComplianceBatchHandler handler) {
		this.next = handler;
	}

	@Override
	public ComplianceBatchHandler getNext() {
		return next;
	}

	@Override
	public String toString() {
		return "ClientTrackingHandler [next=" + next + "]";
	}

	@Override
	public void handleRequest(ComplianceEvent event) {
		log.info(">>> Handle Request: ClientTrackingHandler:  Data: " + event.toString());
	}

}
