package com.lithium.streams.compliance.beans;

import java.util.List;

public interface ConsumeEventsService {

	public abstract List<String> consumeLiaActivitySteamsEvents(String communityName, String login);

	public abstract List<String> consumeLiaActivitySteamsEvents(String communityName, String login, String user);

}
