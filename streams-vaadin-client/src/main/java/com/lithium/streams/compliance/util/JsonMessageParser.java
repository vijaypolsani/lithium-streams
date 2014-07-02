package com.lithium.streams.compliance.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lithium.streams.compliance.model.ActivityStreamv1;

public final class JsonMessageParser {
	private final static ObjectMapper mapper = new ObjectMapper();

	public static ActivityStreamv1 parseIncomingsJsonStreamToObject(final String inputJsonStream) throws IOException {
		ActivityStreamv1 activityStreams = mapper.readValue(inputJsonStream, ActivityStreamv1.class);
		return activityStreams;
	}
}
