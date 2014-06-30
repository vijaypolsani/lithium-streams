package com.lithium.streams.compliance.util;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

public class RestClient {

	public static void main(String args[]) {
		Thread et = new Thread(new AsyncRequestProcessor());
		et.start();
	}

}

class AsyncRequestProcessor implements Runnable {
	private static final String STREAMS_URL = "https://qa.lcloud.com/compliance/v1/live";

	public AsyncRequestProcessor() {
		super();
	}

	public void run() {
		Client client = ClientBuilder.newBuilder().register(SseFeature.SERVER_SENT_EVENTS).build();
		final javax.ws.rs.client.WebTarget webTarget;
		try {
			webTarget = client.target(new URI(STREAMS_URL));
			EventSource eventSource = new EventSource(webTarget) {
				@Override
				public void onEvent(InboundEvent inboundEvent) {
					System.out.println(">>> Event Data: " + inboundEvent.readData());
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}