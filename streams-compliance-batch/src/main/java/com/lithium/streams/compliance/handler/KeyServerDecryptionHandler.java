package com.lithium.streams.compliance.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.api.ComplianceBatchHandler;
import com.lithium.streams.compliance.api.ComplianceEvent;

public class KeyServerDecryptionHandler implements ComplianceBatchHandler {
	private static final Logger log = LoggerFactory.getLogger(KeyServerDecryptionHandler.class);
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
		return "KeyServerDecryptionHandler [next=" + next + "]";
	}

	@Override
	public ComplianceEvent handleRequest(ComplianceEvent event) {
		log.debug(">>> Handle Request: KeyServerDecryptionHandler:  Data: " + event.toString());
		return event;
	}
}
