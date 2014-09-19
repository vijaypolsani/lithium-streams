package com.lithium.streams.compliance.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lithium.streams.compliance.api.ComplianceEvent;
import com.lithium.streams.compliance.model.ComplianceMessage;

public class JsonFormatToArray {
	private static final Logger log = LoggerFactory.getLogger(JsonFormatToArray.class);
	private final static ObjectMapper mapper = new ObjectMapper();
	static {
		//configure Object mapper for pretty print
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static String convertToJsonArray(Collection<ComplianceMessage> messages) {
		//JSONArray jsArray = new JSONArray(messages);
		String returnData = "";
		List<String> events = new ArrayList<String>();
		for (ComplianceEvent msg : messages)
			events.add(new String(msg.getEventPayload().getJsonMessage()));
		try {
			returnData = mapper.writeValueAsString(events.toArray());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			returnData = e.getLocalizedMessage();
		}
		log.debug(returnData);
		return returnData;
	}
}
