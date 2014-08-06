package com.lithium.streams.compliance.filter;

import java.util.Collection;

import com.lithium.streams.compliance.model.ComplianceMessage;

public class Result {
	private final Collection<ComplianceMessage> messages;
	private final long duration;

	public Result(Collection<ComplianceMessage> messages, long duration) {
		this.messages = messages;
		this.duration = duration;
	}

	/**
	 * @return the messages
	 */
	public Collection<ComplianceMessage> getMessages() {
		return messages;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return "Result [messages=" + messages + ", duration=" + duration + "]";
	}

}
