package com.lithium.streams.compliance.model;

import java.io.Serializable;

public final class CompliancePayload implements Serializable {

	private static final long serialVersionUID = 5909515990836686399L;
	private final byte[] jsonMessage;

	private CompliancePayload(byte[] jsonMessage) {
		this.jsonMessage = jsonMessage;
	}

	public static CompliancePayload init(byte[] jsonMessage) {
		return new CompliancePayload(jsonMessage);
	}

	public byte[] getJsonMessage() {
		return jsonMessage;
	}

}
