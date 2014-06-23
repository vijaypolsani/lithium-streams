package com.lithium.streams.compliance.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lithium.streams.compliance.model.ActivityStreams;

public final class JsonMessageParser {
	private final static ObjectMapper mapper = new ObjectMapper();

	public static ActivityStreams parseIncomingsJsonStreamToObject(final String inputJsonStream) throws IOException {
		ActivityStreams activityStreams = mapper.readValue(inputJsonStream, ActivityStreams.class);
		return activityStreams;
	}
}
