package com.lithium.streams.compliance.filter;

import com.lithium.streams.compliance.model.ComplianceMessage;

public class WorkResponse {

	private final ComplianceMessage unfilteredMessage;
	private final long startTime;
	private final long endTime;
	private final boolean isMatching;

	private final int start;

	public WorkResponse(final Work work, final boolean isMatching) {
		this.start = work.getStart();
		this.unfilteredMessage = work.getUnfilteredMessage();
		this.startTime = work.getStartTime();
		this.endTime = work.getEndTime();
		this.isMatching = isMatching;
	}

	public WorkResponse(final int start, final ComplianceMessage msg, final long startTime, final long endTime,
			final boolean isMatching) {
		this.start = start;
		this.unfilteredMessage = msg;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isMatching = isMatching;
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

	/**
	 * @return the isMatching
	 */
	public boolean isMatching() {
		return isMatching;
	}

	@Override
	public String toString() {
		return "Work [start=" + start + ", startTime=" + startTime + ", endTime=" + endTime + ", unfilteredMessage="
				+ unfilteredMessage + "]";
	}

}
