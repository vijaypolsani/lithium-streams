package com.lithium.streams.compliance.filter;

import com.lithium.streams.compliance.model.ComplianceMessage;

public class Work {

	private final ComplianceMessage unfilteredMessage;
	private final long startTime;
	private final long endTime;

	private final int start;

	public Work(int start, ComplianceMessage msg, long startTime, long endTime) {
		this.start = start;
		this.unfilteredMessage = msg;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public int getStart() {
		return start;
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

	/**
	 * @return the unfilteredMessages
	 */
	public ComplianceMessage getUnfilteredMessage() {
		return unfilteredMessage;
	}

	@Override
	public String toString() {
		return "Work [start=" + start + ", startTime=" + startTime + ", endTime=" + endTime + ", unfilteredMessage="
				+ unfilteredMessage + "]";
	}

}
