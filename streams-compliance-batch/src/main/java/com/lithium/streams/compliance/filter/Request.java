package com.lithium.streams.compliance.filter;

import java.util.Collection;

import com.lithium.streams.compliance.model.ComplianceMessage;

public class Request {
	private final Collection<ComplianceMessage> unfilteredMessages;
	private final long startTime;
	private final long endTime;

	public Request(Collection<ComplianceMessage> unfilteredMessages, long startTime, long endTime) {
		this.unfilteredMessages = unfilteredMessages;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * @return the unfilteredMessages
	 */
	public Collection<ComplianceMessage> getUnfilteredMessages() {
		return unfilteredMessages;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
}
