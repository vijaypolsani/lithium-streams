package com.lithium.streams.compliance.model;

import java.io.Serializable;

public final class CompliancePayload implements Serializable {

	private static final long serialVersionUID = 5909515990836686399L;
	private final String jsonMessage;

	public CompliancePayload(String jsonMessage) {
		this.jsonMessage = jsonMessage;
	}

	public String getJsonMessage() {
		return jsonMessage;
	}

}