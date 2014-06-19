package com.lithium.streams.compliance.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;

public interface FilteringService {

	public static final String SAMPLE_INPUT = "{\"event\":{\"id\":460849317,\"type\":\"ActionStart\",\"time\":1391213860448,\"frameId\":460849312,\"version\":\"13.11.0\",\"service\":\"lia\",\"source\":\"sathi.qa\",\"node\":\"9505DD71\"},\"payload\":{\"actionId\":460849313,\"actionKey\":\"nodes.update-node\",\"user\":{\"type$model\":\"user\",\"type\":\"user\",\"uid\":-1,\"registrationStatus\":\"anonymous\",\"login\":\"Anonymous\",\"email\":\"\",\"rankings\":[{\"ranking\":{\"type$model\":\"ranking\",\"type\":\"ranking\",\"uid\":-1,\"name\":\"N/A\"},\"node\":{\"type$model\":\"node\",\"type\":\"community\",\"uid\":1}}],\"roles\":[]}}}";

	public abstract boolean isLiaEventBelongsToId(String inputRawJson, String communityId) throws JsonParseException, IOException;

	public abstract String parseIncomingsJsonStreamForCommunityId(String incomingData) throws IOException;

}
