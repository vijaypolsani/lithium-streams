package com.lithium.streams.compliance.model;

import java.io.Serializable;

import com.lithium.streams.compliance.api.ComplianceEvent;

public final class ComplianceMessage implements ComplianceEvent, Serializable {

	private static final long serialVersionUID = 3296061517542908620L;
	private final String trackingId;
	private final ComplianceHeader complianceHeader;
	private final CompliancePayload compliancePayload;

	private ComplianceMessage(MsgBuilder msgBuilder) {
		this.trackingId = msgBuilder.trackingId;
		this.complianceHeader = msgBuilder.complianceHeader;
		this.compliancePayload = msgBuilder.compliancePayload;
	}

	@Override
	public ComplianceMessage getEvent() {
		//Build first to get the object
		return this;
	}

	@Override
	public ComplianceHeader getEventHeader() {
		//Build first to get the object
		return complianceHeader;
	}

	@Override
	public CompliancePayload getEventPayload() {
		//Build first to get the object
		return compliancePayload;
	}

	public static class MsgBuilder {
		private final String trackingId;
		private ComplianceHeader complianceHeader;
		private CompliancePayload compliancePayload;

		public MsgBuilder(final String trackingId) {
			this.trackingId = trackingId;
		}

		public MsgBuilder header(final ComplianceHeader complianceHeader) {
			this.complianceHeader = complianceHeader;
			return this;
		}

		public MsgBuilder payload(final CompliancePayload compliancePayload) {
			this.compliancePayload = compliancePayload;
			return this;
		}

		public ComplianceMessage build() {
			return new ComplianceMessage(this);
		}

	}
}
