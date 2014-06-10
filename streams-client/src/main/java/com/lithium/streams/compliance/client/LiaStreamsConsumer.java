package com.lithium.streams.compliance.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiaStreamsConsumer {

	public static final String RAW_URL = "http://localhost:8080/streams-archive-web/lia/raw/sathi.qa";
	public static final String STREAMS_URL = "http://localhost:8080/streams-archive-web/lia/streams/sathi.qa";

	private static final Logger log = LoggerFactory.getLogger(LiaStreamsConsumer.class);

	public static void main(String[] args) {
		Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
		WebTarget target = client.target(STREAMS_URL);
		log.info(">>> Prepared Target: " + target.getUri());
		final EventSource eventSource = new EventSource(target);
		log.info(">>> Registered Endpoint Reconnect Interval s: " + eventSource.RECONNECT_DEFAULT);
		log.info(">>> Registered Endpoint & Open. Waiting");
		eventSource.register(new KeepListening());
	}

	public static class KeepListening implements EventListener {
		@Override
		public void onEvent(InboundEvent inboundEvent) {
			try {
				log.info(">>> Data received from REST Server: " + inboundEvent.readData());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
