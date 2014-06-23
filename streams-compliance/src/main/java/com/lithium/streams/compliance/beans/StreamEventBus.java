package com.lithium.streams.compliance.beans;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.MoreExecutors;
import com.lithium.streams.compliance.model.ComplianceEvent;
import com.lithium.streams.compliance.util.StreamEventBusListener;

public class StreamEventBus {

	public static final String LIA_BUS_NAME = "ComplianceEventBus";
	//private EventBus eventBus = new EventBus(LIA_BUS_NAME);
	private final EventBus eventBus;

	public StreamEventBus() {
		eventBus = new AsyncEventBus(MoreExecutors.sameThreadExecutor());
	}

	public void unRegisterSubscriber(ComplianceEvent comlianceEvent) {
		eventBus.unregister(comlianceEvent);
	}

	public void postEvent(ComplianceEvent comlianceEvent) {
		eventBus.post(comlianceEvent);
	}

	public void registerSubscriber(StreamEventBusListener streamEventBusListener) {
		eventBus.register(streamEventBusListener);
	}

}
