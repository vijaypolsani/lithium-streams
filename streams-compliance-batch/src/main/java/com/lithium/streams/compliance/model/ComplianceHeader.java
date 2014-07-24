package com.lithium.streams.compliance.model;

import java.math.BigInteger;

public final class ComplianceHeader {

	private long timeStampMilliSec;
	private String communityId;
	private BigInteger sequenceId;

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

	public BigInteger getSequenceId() {
		return sequenceId;
	}

	public static class HeaderBuilder {
		private final long timeStampMilliSec;
		private BigInteger sequenceId;
		private String communityId;

		public HeaderBuilder(long timeStampMilliSec) {
			this.timeStampMilliSec = timeStampMilliSec;
		}

		public HeaderBuilder sequenceId(BigInteger sequenceId) {
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
