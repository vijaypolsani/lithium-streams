package com.lithium.streams.compliance.util;

import com.lithium.streams.compliance.model.ComplianceEvent;

public interface StreamEventBusListener {
	public void readEvents(ComplianceEvent complianceEvent);
}