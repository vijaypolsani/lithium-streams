package com.lithium.streams.compliance.handler;

import com.lithium.streams.compliance.api.ComplianceBatchHandler;
import com.lithium.streams.compliance.model.ComplianceMessage;

public final class ComplainceHandlerProcessor {
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
		while (temp != null) {
			temp = nextHandler;
			if (nextHandler != null)
				nextHandler = nextHandler.getNext();
			if (temp != null) {
				System.out.print("\nReturned Object: " + temp + "\n");
				temp.handleRequest(complianceMessage);
			}
		}
		return temp;
	}

	public void printHandlerChain() {
		System.out.print("List: " + nextHandler.toString() + "\n");
	}

}
