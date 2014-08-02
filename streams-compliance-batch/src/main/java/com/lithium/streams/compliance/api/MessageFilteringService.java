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
	public static final String SAMPLE_INPUT = "{\"event\":{\"id\":460849317,\"type\":\"ActionStart\",\"time\":1391213860448,\"frameId\":460849312,\"version\":\"13.11.0\",\"service\":\"lia\",\"source\":\"sathi.qa\",\"node\":\"9505DD71\"},\"payload\":{\"actionId\":460849313,\"actionKey\":\"nodes.update-node\",\"user\":{\"type$model\":\"user\",\"type\":\"user\",\"uid\":-1,\"registrationStatus\":\"anonymous\",\"login\":\"Anonymous\",\"email\":\"\",\"rankings\":[{\"ranking\":{\"type$model\":\"ranking\",\"type\":\"ranking\",\"uid\":-1,\"name\":\"N/A\"},\"node\":{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1}}],\"roles\":[]}}}";

	public static boolean filterByTime(String inputJsonStream, long startTime, long endTime) throws IOException {
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
		checkArgument(startTime <= 0, "Start time cannot be negative or 0. StartTime:  %d", startTime);
		checkArgument(endTime <= 0, "End time cannot be negative or 0. EndTime:  %d", endTime);
		checkArgument(startTime > endTime, "Start time cannot be later than end Time. StartTime: EndTime %d, %d",
				startTime, endTime);

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
					if ((startTime <= messageCurrentTime) && (endTime <= messageCurrentTime))
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

	public default boolean filterBySequenceId(String inputJsonStream, long startId, long endId) throws IOException {
		throw new UnsupportedOperationException("SequenceID based querying not supported in this release.");
	}
}
