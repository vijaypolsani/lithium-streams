package com.lithium.streams.compliance.model;

import java.io.Serializable;
import java.math.BigInteger;

public final class ComplianceHeader implements Serializable {

	private static final long serialVersionUID = -5815127080811376037L;
	private long timeStampMilliSec;
	private String communityId;
	private long sequenceId;

	private ComplianceHeader(HeaderBuilder builder) {
		this.timeStampMilliSec = builder.timeStampMilliSec;
		this.communityId = builder.communityId;
		this.sequenceId = builder.sequenceId;
	}

	public long getTimeStampMilliSec() {
		return timeStampMilliSec;
	}

	public String getCommunityId() {
		return communityId;
	}

	public long getSequenceId() {
		return sequenceId;
	}

	public static class HeaderBuilder {
		private final long timeStampMilliSec;
		private long sequenceId;
		private String communityId;

		public HeaderBuilder(long timeStampMilliSec) {
			this.timeStampMilliSec = timeStampMilliSec;
		}

		public HeaderBuilder sequenceId(long sequenceId) {
			this.sequenceId = sequenceId;
			return this;
		}

		public HeaderBuilder communityId(String communityId) {
			this.communityId = communityId;
			return this;
		}

		public ComplianceHeader build() {
			return new ComplianceHeader(this);
		}
	}
}
