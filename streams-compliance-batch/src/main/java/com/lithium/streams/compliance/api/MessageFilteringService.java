package com.lithium.streams.compliance.api;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;

public interface MessageFilteringService {
	public static final Logger log = LoggerFactory.getLogger(MessageFilteringService.class);
	public static final JsonFactory jsonFactory = new JsonFactory();
	public static final DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
	public static final Calendar cal = Calendar.getInstance();
	public static final String SAMPLE_INPUT = "{\"formatVersion\":\"1.0\",\"published\":\"1417440671553\",\"generator\":{\"source\":\"actiance.stage\",\"eventId\":\"3842328\"},\"provider\":{\"service\":\"lia\",\"version\":\"14.6.6\"},\"title\":\"EntityUpdated\",\"actor\":{\"uid\":\"1\",\"login\":\"admin\",\"registrationStatus\":\"FULLY_REGISTERED\",\"email\":\"admin@lithium.com\",\"type\":\"user\",\"registrationTime\":\"1402004942097\"},\"verb\":\"EntityUpdated user\",\"target\":{\"type\":\"user\",\"conversationType\":\"\",\"id\":\"1\",\"conversationId\":\"\"},\"streamObject\":{\"objectType\":\"\",\"id\":\"\",\"displayName\":\"\",\"content\":\"\",\"visibility\":\"\",\"subject\":\"\",\"added\":\"\",\"postTime\":\"\",\"isTopic\":\"false\"}}";
	public static final String SAMPLE_INPUT1 = "{\"event\":{\"id\":244806492,\"type\":\"Blackbox\",\"time\":1417632301553,\"frameId\":244806491,\"version\":\"14.10.0\",\"service\":\"lia\",\"source\":\"actiance.stage\",\"node\":\"2E8225CA\"},\"payload\":{\"line\":\"stats 1417632301553 0 1251162 0 0 0 - 0 0 0\"}}";

	public static boolean filterByTime(final byte[] inputJsonStream, final long startTime, final long endTime)
			throws IOException {
		jsonFactory.enable(Feature.ALLOW_COMMENTS);
		jsonFactory.enable(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
		jsonFactory.enable(Feature.ALLOW_SINGLE_QUOTES);
		jsonFactory.enable(Feature.ALLOW_NUMERIC_LEADING_ZEROS);
		jsonFactory.enable(Feature.ALLOW_NON_NUMERIC_NUMBERS);
		jsonFactory.enable(Feature.ALLOW_SINGLE_QUOTES);
		jsonFactory.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
		jsonFactory.enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
		jsonFactory.enable(Feature.ALLOW_YAML_COMMENTS);
		checkNotNull(inputJsonStream, "Message JSON that need to be parsed cannot be null.");
		checkArgument(startTime >= 0, "Start time cannot be negative or less than 0. StartTime:  %d", startTime);
		checkArgument(endTime >= 0, "End time cannot be negative or less than 0. EndTime:  %d", endTime);
		checkArgument(startTime < endTime, "Expected Start time less than End Time. but %d > %d ", startTime, endTime);

		boolean source = false;
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
				case "published":
					jsonParser.nextToken();
					String published = jsonParser.getText();
					long messageCurrentTime = Long.parseLong(published);

					cal.setTimeInMillis(startTime);
					log.info(">>> Query Start Time: " + df.format(cal.getTime()));

					cal.setTimeInMillis(messageCurrentTime);
					log.info(">>> Published Time: (published): " + df.format(cal.getTime()));

					cal.setTimeInMillis(endTime);
					log.info(">>> Query End Time: " + df.format(cal.getTime()));
					//Matching Logic
					if ((startTime <= messageCurrentTime) && (endTime >= messageCurrentTime))
						source = true;
					break;
				}
				if (source)
					break;
				jsonToken = jsonParser.nextToken();
			}
			jsonParser.close();
		} catch (JsonParseException je) {
			log.error("<<< Exception is filtering the Event Message to parse published time. "
					+ je.getLocalizedMessage());
		}
		return source;
	}

	public static boolean filterEventByTime(final byte[] inputJsonStream, final long startTime, final long endTime)
			throws IOException {
		jsonFactory.enable(Feature.ALLOW_COMMENTS);
		jsonFactory.enable(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
		jsonFactory.enable(Feature.ALLOW_SINGLE_QUOTES);
		jsonFactory.enable(Feature.ALLOW_NUMERIC_LEADING_ZEROS);
		jsonFactory.enable(Feature.ALLOW_NON_NUMERIC_NUMBERS);
		jsonFactory.enable(Feature.ALLOW_SINGLE_QUOTES);
		jsonFactory.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
		jsonFactory.enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
		jsonFactory.enable(Feature.ALLOW_YAML_COMMENTS);
		checkNotNull(inputJsonStream, "Message JSON that need to be parsed cannot be null.");
		checkArgument(startTime >= 0, "Start time cannot be negative or less than 0. StartTime:  %d", startTime);
		checkArgument(endTime >= 0, "End time cannot be negative or less than 0. EndTime:  %d", endTime);
		checkArgument(startTime < endTime, "Expected Start time less than End Time. but %d > %d ", startTime, endTime);

		boolean source = false;
		try {
			JsonParser jsonParser = jsonFactory.createParser(inputJsonStream);
			JsonToken jsonToken = jsonParser.nextToken();
			if (jsonToken.equals(JsonToken.START_OBJECT) || jsonToken.equals(JsonToken.VALUE_NULL)
					|| jsonToken.equals(JsonToken.END_OBJECT))
				// Had to put source == false check to exit before Payload Field. As it is causing issue in parsing with improper format.
				jsonToken = jsonParser.nextToken();
			while (jsonParser.hasCurrentToken() && jsonToken != JsonToken.VALUE_NULL) {
				String fieldName = jsonParser.getCurrentName();
				if (jsonToken == null || fieldName == null)
					break;
				switch (fieldName) {
				case "time":
					jsonParser.nextToken();
					String published = jsonParser.getText();
					if (published != null && published != "") {
						long messageCurrentTime = Long.parseLong(published);

						cal.setTimeInMillis(startTime);
						log.info(">>> Query Start Time: " + df.format(cal.getTime()));

						cal.setTimeInMillis(messageCurrentTime);
						log.info(">>> Published Time: (published): " + df.format(cal.getTime()));

						cal.setTimeInMillis(endTime);
						log.info(">>> Query End Time: " + df.format(cal.getTime()));
						//Matching Logic
						if ((startTime <= messageCurrentTime) && (endTime >= messageCurrentTime))
							source = true;
					}
					break;
				}
				if (source)
					break;
				jsonToken = jsonParser.nextToken();
			}
			jsonParser.close();
		} catch (JsonParseException je) {
			log.error("<<< Exception is filtering the Event Message to parse published time. "
					+ je.getLocalizedMessage());
		}
		return source;
	}

	public default boolean filterBySequenceId(byte[] inputJsonStream, long startId, long endId) throws IOException {
		throw new UnsupportedOperationException("SequenceID based querying not supported in this release.");
	}
}
