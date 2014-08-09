package com.lithium.streams.compliance.api;

import java.util.Collection;

import com.lithium.streams.compliance.model.ComplianceMessage;

public interface FilterMessages {
	public Collection<ComplianceMessage> receiveMessages(Collection<ComplianceMessage> unfilteredMessages,
			long startTime, long endTime);
}
