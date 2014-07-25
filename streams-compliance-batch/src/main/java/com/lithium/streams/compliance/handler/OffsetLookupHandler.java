package com.lithium.streams.compliance.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.api.ComplianceBatchHandler;
import com.lithium.streams.compliance.api.ComplianceEvent;

public class OffsetLookupHandler implements ComplianceBatchHandler {

	private static final Logger log = LoggerFactory.getLogger(OffsetLookupHandler.class);
	private ComplianceBatchHandler next;

	@Override
	public void setNext(ComplianceBatchHandler handler) {
		this.next = handler;
	}

	@Override
	public void handleRequest(ComplianceEvent event) {
		log.info(">>> Handle Request: OffsetLookupHandler: " + event.getEvent());
	}

	@Override
	public ComplianceBatchHandler getNext() {
		return next;
	}

	@Override
	public String toString() {
		return "OffsetLookupHandler [next=" + next + "]";
	}

}
