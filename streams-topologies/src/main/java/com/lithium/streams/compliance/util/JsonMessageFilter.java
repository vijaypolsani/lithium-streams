package com.lithium.streams.compliance.util;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public final class JsonMessageFilter {
	private static final JsonFactory jsonFactory = new JsonFactory();
	private static final Logger log = LoggerFactory.getLogger(JsonMessageFilter.class);
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

	}
	public static final String SAMPLE_INPUT = "{\"event\":{\"id\":32250902,\"type\":\"ActionStart\",\"time\":1400014566536,\"frameId\":32250849,\"version\":\"14.5.0\",\"service\":\"lia\",\"source\":\"lsw.qa\",\"node\":\"A65670F2\"},\"payload\":{\"actionId\":32250901,\"actionKey\":\"kudos.give-message-kudo\",\"user\":{\"type$model\":\"user\",\"type\":\"user\",\"uid\":7,\"registrationStatus\":\"fully-registered\",\"registration.time\":\"2014-05-13T20:56:04.715Z\",\"login\":\"testuserdemo1\",\"email\":\"megha.meshram@lithium.com\",\"rankings\":[{\"ranking\":{\"type$model\":\"ranking\",\"type\":\"ranking\",\"uid\":23,\"name\":\"New Member\"},\"node\":{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1}}],\"roles\":[]}}}";

	public JsonMessageFilter() {
	}

	public boolean parseIncomingsJsonStream(final String inputJsonStream, final String matchingId) throws IOException {
		checkNotNull(inputJsonStream, "Message JSON that need to be parsed cannot be null.");
		checkNotNull(matchingId, "Message JSON filter criteria cannot be null.");

		Boolean source = false;
		try {
			JsonParser jsonParser = jsonFactory.createParser(inputJsonStream);
			JsonToken jsonToken = jsonParser.nextToken();
			if (jsonToken.equals(JsonToken.START_OBJECT) || jsonToken.equals(JsonToken.VALUE_NULL)
					|| jsonToken.equals(JsonToken.END_OBJECT))
				jsonToken = jsonParser.nextToken();
			// Had to put source == false check to exit before Payload Field. As it is causing issue in parsing with improper format.
			while (jsonParser.hasCurrentToken() && jsonToken != JsonToken.VALUE_NULL && source == false) {
				String fieldName = jsonParser.getCurrentName();
				if (jsonToken == null || fieldName == null)
					break;
				switch (fieldName) {
				case "source":
					jsonParser.nextToken();
					log.info(">>> Community ID: (source): " + jsonParser.getText());
					if (matchingId.equalsIgnoreCase(jsonParser.getText()))
						source = true;
					break;
				}
				if (source)
					break;
				jsonToken = jsonParser.nextToken();
			}
			jsonParser.close();
		} catch (JsonParseException je) {
			log.error("<<< Exception is parsing the Event Logger Message: Skipping this message at Origination ..."
					+ je.getLocalizedMessage());
		}
		return source;
	}

	public String parseIncomingsJsonStreamForCommunityId(final String inputJsonStream) throws IOException {
		String source = null;
		try {
			JsonParser jsonParser = jsonFactory.createParser(inputJsonStream);
			JsonToken jsonToken = jsonParser.nextToken();
			if (jsonToken.equals(JsonToken.START_OBJECT) || jsonToken.equals(JsonToken.VALUE_NULL)
					|| jsonToken.equals(JsonToken.END_OBJECT))
				jsonToken = jsonParser.nextToken();
			// Had to put source == null check to exit before Payload Field. As it is causing issue in parsing with improper format.
			while (jsonParser.hasCurrentToken() && jsonToken != JsonToken.VALUE_NULL && source == null) {
				String fieldName = jsonParser.getCurrentName();
				if (jsonToken == null || fieldName == null)
					break;
				switch (fieldName) {
				case "source":
					jsonParser.nextToken();
					log.info(">>> Community ID: (source): " + jsonParser.getText());
					source = jsonParser.getText();
					break;
				}
				if (source != null)
					break;
				jsonToken = jsonParser.nextToken();
			}
			jsonParser.close();
		} catch (JsonParseException je) {
			log.error("<<< Exception is parsing the Event Logger Message: Skipping this message at Origination ..."
					+ je.getLocalizedMessage());
		}
		return source;
	}

	public static void main(String args[]) {
		try {
			log.info(">>> Result contains filter word ? : "
					+ new JsonMessageFilter().parseIncomingsJsonStream(SAMPLE_INPUT, "sathi.qa"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
