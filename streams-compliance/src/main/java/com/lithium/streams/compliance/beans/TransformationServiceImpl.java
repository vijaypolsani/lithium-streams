package com.lithium.streams.compliance.beans;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.lithium.streams.compliance.util.JsonMessageParser;

public class TransformationServiceImpl implements TransformationService {
	private final JsonMessageParser jsonMessageParser = new JsonMessageParser();

	@Override
	public String transformToActivityStreams1(String rawEvent) throws JsonParseException, IOException {
		return jsonMessageParser.parseIncomingsJsonStream(rawEvent);
	}

	@Override
	public String transformToActivityStreams2(String rawEvent) throws JsonParseException, IOException {
		return jsonMessageParser.parseIncomingsJsonStream(rawEvent);
	}

}
