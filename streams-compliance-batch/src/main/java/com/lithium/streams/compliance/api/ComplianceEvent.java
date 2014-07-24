package com.lithium.streams.compliance.api;

import com.lithium.streams.compliance.model.ComplianceHeader;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.model.CompliancePayload;

public interface ComplianceEvent {

	public abstract ComplianceMessage getEvent();

	public abstract ComplianceHeader getEventHeader();

	public abstract CompliancePayload getEventPayload();
}
