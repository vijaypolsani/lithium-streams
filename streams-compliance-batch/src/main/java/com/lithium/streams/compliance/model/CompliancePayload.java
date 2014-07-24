package com.lithium.streams.compliance.model;

public final class CompliancePayload {

	private final String jsonMessage;

	public CompliancePayload(String jsonMessage) {
		this.jsonMessage = jsonMessage;
	}

	public String getJsonMessage() {
		return jsonMessage;
	}

}
