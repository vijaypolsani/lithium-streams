package com.lithium.streams.compliance.api;


public interface ComplianceBatchHandler {

	public abstract void setNext(ComplianceBatchHandler handler);

	public abstract ComplianceBatchHandler getNext();

	public abstract void handleRequest(ComplianceEvent event);
}
