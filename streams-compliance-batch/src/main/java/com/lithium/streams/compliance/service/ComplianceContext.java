package com.lithium.streams.compliance.service;

public class ComplianceContext {
	private final String topicName;
	private final String startTime;
	private final String endTime;

	public ComplianceContext(String topicName, String startTime, String endTime) {
		this.topicName = topicName;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

}
