package com.lithium.streams.compliance.util;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lithium.streams.compliance.model.ActivityStreams;

public final class JsonMessageParser {
	private static final JsonFactory jsonFactory = new JsonFactory();
	private static final Logger log = LoggerFactory.getLogger(JsonMessageParser.class);
	private final ActivityStreams activityStreams = new ActivityStreams();
	private final static ObjectMapper mapper = new ObjectMapper();
	private static final DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
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
	public static final String SAMPLE_INPUT1 = "{\"event\":{\"id\":32250902,\"type\":\"ActionStart\",\"time\":1400014566536,\"frameId\":32250849,\"version\":\"14.5.0\",\"service\":\"lia\",\"source\":\"lsw.qa\",\"node\":\"A65670F2\"},\"payload\":{\"actionId\":32250901,\"actionKey\":\"kudos.give-message-kudo\",\"user\":{\"type$model\":\"user\",\"type\":\"user\",\"uid\":7,\"registrationStatus\":\"fully-registered\",\"registration.time\":\"2014-05-13T20:56:04.715Z\",\"login\":\"testuserdemo1\",\"email\":\"megha.meshram@lithium.com\",\"rankings\":[{\"ranking\":{\"type$model\":\"ranking\",\"type\":\"ranking\",\"uid\":23,\"name\":\"New Member\"},\"node\":{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1}}],\"roles\":[]}}}";
	public static final String SAMPLE_INPUT2 = "{\"event\":{\"id\":34016437,\"type\":\"EntityCreated\",\"time\":1400101236869,\"frameId\":34016419,\"version\":\"14.5.0\",\"service\":\"lia\",\"source\":\"lsw.qa\",\"node\":\"A65670F2\"},\"payload\":{\"actionId\":5052,\"target\":{\"type$model\":\"message\",\"type\":\"message\",\"uid\":19,\"node\":{\"type$model\":\"node\",\"type\":\"forum-board\",\"uid\":5,\"id\":\"General\",\"title\":\"A Test Board\",\"ancestors\":[{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1},{\"type$model\":\"node\",\"type\":\"category\",\"uid\":2}]},\"num\":8,\"visibility\":\"public\",\"author\":{\"type$model\":\"user\",\"type\":\"user\",\"uid\":7,\"registrationStatus\":\"fully-registered\",\"registration.time\":\"2014-05-13T20:56:04.715Z\",\"login\":\"testuserdemo1\",\"email\":\"megha.meshram@lithium.com\",\"rankings\":[{\"ranking\":{\"type$model\":\"ranking\",\"type\":\"ranking\",\"uid\":23,\"name\":\"New Member\"},\"node\":{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1}}],\"roles\":[]},\"subject\":\"Testing demo message\",\"post.time\":\"2014-05-14T21:00:36.577Z\",\"conversationStyle\":\"forum\",\"isTopic\":true,\"conversation\":{\"type$model\":\"conversation\",\"type\":\"conversation\",\"uid\":19,\"topic\":{\"type$model\":\"message\",\"type\":\"message\",\"uid\":19},\"node\":{\"type$model\":\"node\",\"type\":\"forum-board\",\"uid\":5},\"style\":\"forum\"},\"message-type\":\"topic\"}}}";
	public static final String SAMPLE_INPUT3 = "{\"event\":{\"id\":34016465,\"type\":\"Blackbox\",\"time\":1400101236974,\"frameId\":34016419,\"version\":\"14.5.0\",\"service\":\"lia\",\"source\":\"lsw.qa\",\"node\":\"A65670F2\"},\"payload\":{\"line\":\"\"}}";

	public JsonMessageParser() {
	}

	public String parseIncomingsJsonStream(final String inputJsonStream) throws IOException {
		try {
			JsonParser jsonParser = jsonFactory.createParser(inputJsonStream);
			JsonToken jsonToken = jsonParser.nextToken();
			if (jsonToken.equals(JsonToken.START_OBJECT) || jsonToken.equals(JsonToken.VALUE_NULL)
					|| jsonToken.equals(JsonToken.END_OBJECT))
				jsonToken = jsonParser.nextToken();

			while (jsonParser.hasCurrentToken() && jsonToken != JsonToken.VALUE_NULL) {
				String fieldName = jsonParser.getCurrentName();
				if (jsonToken == null || fieldName == null)
					break;
				switch (fieldName) {
				case "id":
					jsonParser.nextToken();
					log.info("id: " + jsonParser.getText());
					activityStreams.getActor().setId(jsonParser.getText());
					break;
				case "type":
					jsonParser.nextToken();
					log.info("type: " + jsonParser.getText());
					if (activityStreams.getTitle() == null)
						activityStreams.setTitle(jsonParser.getText());
					break;
				case "time":
					jsonParser.nextToken();
					log.info("time: " + jsonParser.getText());
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.parseLong(jsonParser.getText()));
					activityStreams.setPublished(formatter.format(cal.getTime()));
					break;
				case "frameId":
					jsonParser.nextToken();
					log.info("frameId: " + jsonParser.getText());
					activityStreams.getActor().setFrameId(jsonParser.getText());
					break;
				case "version":
					jsonParser.nextToken();
					log.info("version: " + jsonParser.getText());
					activityStreams.getTarget().setVersion(jsonParser.getText());
					break;
				case "service":
					jsonParser.nextToken();
					log.info("service: " + jsonParser.getText());
					activityStreams.getTarget().setService(jsonParser.getText());
					activityStreams.getProvider().setService(jsonParser.getText());
					break;
				case "source":
					jsonParser.nextToken();
					log.info("source: " + jsonParser.getText());
					activityStreams.getTarget().setSource(jsonParser.getText());
					activityStreams.getGenerator().setSource(jsonParser.getText());
					break;
				case "node":
					jsonParser.nextToken();
					log.info("node: " + jsonParser.getText());
					activityStreams.getTarget().setNode(jsonParser.getText());
					break;
				case "actionId":
					jsonParser.nextToken();
					log.info("actionId: " + jsonParser.getText());
					activityStreams.getObj().setActionId(jsonParser.getText());
					break;
				case "actionKey":
					jsonParser.nextToken();
					log.info("actionKey: " + jsonParser.getText());
					activityStreams.getObj().setActionKey(jsonParser.getText());
					activityStreams.setVerb(jsonParser.getText());
					break;
				case "actionResult":
					jsonParser.nextToken();
					log.info("actionResult: " + jsonParser.getText());
					activityStreams.getObj().setActionResult(jsonParser.getText());
					break;
				case "type$model":
					jsonParser.nextToken();
					log.info("type$model: " + jsonParser.getText());
					activityStreams.getActor().setModel(jsonParser.getText());
					break;
				case "uid":
					jsonParser.nextToken();
					log.info("uid: " + jsonParser.getText());
					activityStreams.getActor().setUid(jsonParser.getText());
					break;
				case "registrationStatus":
					jsonParser.nextToken();
					log.info("registrationStatus: " + jsonParser.getText());
					activityStreams.getActor().setRegistrationStatus(jsonParser.getText());
					break;
				case "login":
					jsonParser.nextToken();
					log.info("login: " + jsonParser.getText());
					activityStreams.getActor().setLogin(jsonParser.getText());
					break;
				case "email":
					jsonParser.nextToken();
					log.info("email: " + jsonParser.getText());
					activityStreams.getActor().setEmail(jsonParser.getText());
					break;
				case "name":
					jsonParser.nextToken();
					log.info("name: " + jsonParser.getText());
					activityStreams.getActor().setName(jsonParser.getText());
					break;

				/*	case "line":
						jsonParser.nextToken();
						log.info("line: " + jsonParser.getText());
						activityStreams.setExtensionElements(jsonParser.getText());
						break;
				*/
				case "num":
					jsonParser.nextToken();
					log.info("num: " + jsonParser.getText());
					activityStreams.getTarget().setNum(jsonParser.getText());
					break;
				case "visibility":
					jsonParser.nextToken();
					log.info("visibility: " + jsonParser.getText());
					activityStreams.getTarget().setVisibility(jsonParser.getText());
					break;
				case "subject":
					jsonParser.nextToken();
					log.info("subject: " + jsonParser.getText());
					activityStreams.getTarget().setSubject(jsonParser.getText());
					break;
				case "post.time":
					jsonParser.nextToken();
					log.info("post.time: " + jsonParser.getText());
					activityStreams.getTarget().setPost_time(jsonParser.getText());
					break;
				case "conversationStyle":
					jsonParser.nextToken();
					log.info("conversationStyle: " + jsonParser.getText());
					activityStreams.getTarget().setConversationStype(jsonParser.getText());
					break;
				case "isTopic":
					jsonParser.nextToken();
					log.info("isTopic: " + jsonParser.getText());
					activityStreams.getTarget().setIsTopic(jsonParser.getText());
					break;
				case "message-type":
					jsonParser.nextToken();
					log.info("message-type: " + jsonParser.getText());
					activityStreams.getTarget().setMessage_type(jsonParser.getText());
					break;
				}
				jsonToken = jsonParser.nextToken();
			}
			jsonParser.close();
		} catch (JsonParseException je) {
			log.error("<<< Exception is parsing the Event Logger Message: Skipping this message at Origination ..."
					+ je.getLocalizedMessage());
		}
		return mapper.writeValueAsString(activityStreams);
	}

	public static void main(String args[]) {
		try {
			log.info(">>> Transformed Message: " + new JsonMessageParser().parseIncomingsJsonStream(SAMPLE_INPUT1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
