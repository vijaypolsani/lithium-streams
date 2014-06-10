package com.lithium.streams.compliance.beans;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.lithium.streams.compliance.util.JsonMessageFilter;

public class FilteringServiceImpl implements FilteringService {
	private static final Logger log = LoggerFactory.getLogger(FilteringServiceImpl.class);

	@Override
	public boolean isLiaEventBelongsToId(String inputRawJson, String eventId) throws JsonParseException, IOException {
		log.info(">>> Filtering Service Service looking for community/source as : " + eventId);
		return new JsonMessageFilter().parseIncomingsJsonStream(inputRawJson, eventId);
	}
}
