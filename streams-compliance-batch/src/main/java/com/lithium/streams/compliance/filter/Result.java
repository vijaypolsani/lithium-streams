package com.lithium.streams.compliance.filter;

import com.google.common.collect.ImmutableCollection;
import com.lithium.streams.compliance.model.ComplianceMessage;

public class Result {
	private final ImmutableCollection<ComplianceMessage> messages;
	private final long duration;

	public Result(ImmutableCollection<ComplianceMessage> messages, long duration) {
		this.messages = messages;
		this.duration = duration;
	}

	/**
	 * @return the messages
	 */
	public ImmutableCollection<ComplianceMessage> getMessages() {
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
