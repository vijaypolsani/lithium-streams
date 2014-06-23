package com.lithium.streams.compliance.beans;

import java.util.concurrent.ExecutionException;

import com.lithium.streams.compliance.util.StreamEventBusListener;

public interface ConsumeEventsService {

	public abstract void consumeEvents(String communityName, String login, StreamEventBusListener streamEventBusListener)
			throws InterruptedException, ExecutionException;

}
