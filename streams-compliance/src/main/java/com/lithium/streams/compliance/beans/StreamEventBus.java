package com.lithium.streams.compliance.beans;

import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.MoreExecutors;
import com.lithium.streams.compliance.model.ComplianceEvent;
import com.lithium.streams.compliance.util.StreamEventBusListener;

@Path("bus")
@Component
public class StreamEventBus {

	public static final String LIA_BUS_NAME = "ComplianceEventBus";
	//private EventBus eventBus = new EventBus(LIA_BUS_NAME);
	private final EventBus eventBus;

	public StreamEventBus() {
		//eventBus = new AsyncEventBus(MoreExecutors.sameThreadExecutor());
		eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
		}

	public void unRegisterSubscriber(StreamEventBusListener streamEventBusListener) {
		eventBus.unregister(streamEventBusListener);
	}

	public void postEvent(ComplianceEvent comlianceEvent) {
		eventBus.post(comlianceEvent);
	}

	public void registerSubscriber(StreamEventBusListener streamEventBusListener) {
		eventBus.register(streamEventBusListener);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getStats() {
		return eventBus.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventBus == null) ? 0 : eventBus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StreamEventBus other = (StreamEventBus) obj;
		if (eventBus == null) {
			if (other.eventBus != null)
				return false;
		} else if (!eventBus.equals(other.eventBus))
			return false;
		return true;
	}

}
