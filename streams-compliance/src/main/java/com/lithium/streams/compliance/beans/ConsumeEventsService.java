package com.lithium.streams.compliance.beans;

import java.util.Deque;
import java.util.concurrent.ExecutionException;

public interface ConsumeEventsService {

	public abstract Deque<String> consumeEvents(String communityName, String login) throws InterruptedException, ExecutionException;

}
