package com.lithium.streams.compliance.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lithium.streams.compliance.model.ComplianceEvent;

public class StreamEventBusHelper {
	private static final Logger log = LoggerFactory.getLogger(StreamEventBusHelper.class);

	public StreamEventBusHelper() {
	}

	@Subscribe
	@AllowConcurrentEvents
	public void readEvents(ComplianceEvent complianceEvent) {
		log.info(">>> LiaPostEvent Subscribed: " + complianceEvent.getEvent());
	}

}
