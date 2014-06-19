package com.lithium.streams.compliance.beans;

import java.util.List;

public interface ConsumeEventsService {

	public abstract List<String> consumeEvents(String communityName, String login);

}
