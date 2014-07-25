package com.lithium.streams.compliance.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.api.ComplianceBatchHandler;
import com.lithium.streams.compliance.model.ComplianceMessage;

public final class ComplainceHandlerProcessor {
	private static final Logger log = LoggerFactory.getLogger(ComplainceHandlerProcessor.class);

	private ComplianceBatchHandler nextHandler;

	public ComplainceHandlerProcessor() {
		createProcessorChain();
	}

	private void createProcessorChain() {
		//FILO
		this.addHandler(new HeaderPopulateHandler());
		this.addHandler(new OffsetLookupHandler());
		this.addHandler(new ClientTrackingHandler());
	}

	public void addHandler(ComplianceBatchHandler handler) {
		handler.setNext(nextHandler);
		nextHandler = handler;
	}

	public ComplianceBatchHandler processChain(final ComplianceMessage complianceMessage) {
		ComplianceBatchHandler temp = nextHandler;
		while (nextHandler != null) {
			nextHandler.handleRequest(complianceMessage);
			nextHandler = nextHandler.getNext();
		}
		if (nextHandler == null)
			nextHandler = temp;
		return temp;
	}

	public String printHandlerChain() {
		if (nextHandler != null) {
			log.info(">>> List: " + nextHandler.toString());
			return nextHandler.toString();
		}
		return "";
	}

}