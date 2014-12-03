package com.lithium.streams.compliance.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lithium.streams.compliance.model.ActivityStreamv1;

public final class JsonMessageParser {
	private final static ObjectMapper mapper = new ObjectMapper();
	private static final JsonFactory jsonFactory = new JsonFactory();
	private static final Logger log = LoggerFactory.getLogger(JsonMessageParser.class);
	static {
		jsonFactory.enable(Feature.ALLOW_COMMENTS);
		jsonFactory.enable(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
		jsonFactory.enable(Feature.ALLOW_SINGLE_QUOTES);
		jsonFactory.enable(Feature.ALLOW_NUMERIC_LEADING_ZEROS);
		jsonFactory.enable(Feature.ALLOW_NON_NUMERIC_NUMBERS);
		jsonFactory.enable(Feature.ALLOW_SINGLE_QUOTES);
		jsonFactory.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
		jsonFactory.enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
		jsonFactory.enable(Feature.ALLOW_YAML_COMMENTS);
		//configure Object mapper for pretty print
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static ActivityStreamv1 parseIncomingsJsonStreamToObject(final String inputJsonStream) throws IOException {
		ActivityStreamv1 activityStreams = mapper.readValue(inputJsonStream, ActivityStreamv1.class);
		return activityStreams;
	}
}
