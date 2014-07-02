package com.lithium.streams.compliance.util;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.sse.EventInput;
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
	//private static final String STREAMS_URL = "https://qa.lcloud.com/compliance/v1/live";
	//private static final String STREAMS_URL = "http://10.240.180.18:6060/compliance/v1/live";
	private static final String STREAMS_URL = "http://localhost:6060/compliance/v1/live";

	private static final String CLIENT_TOKEN = "client-id";
	private static final String CLIENT_TOKEN_VALUE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";

	public AsyncRequestProcessor() {
		super();
	}

	public void run() {
		Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
		final javax.ws.rs.client.WebTarget webTarget = client.target(UriBuilder.fromPath(STREAMS_URL));

		try {
			Invocation invocation = webTarget.request().header(CLIENT_TOKEN, CLIENT_TOKEN_VALUE).buildGet();
			EventInput eventInput = invocation.invoke(EventInput.class);
			while (!eventInput.isClosed()) {
				final InboundEvent inboundEvent = eventInput.read();
				if (inboundEvent == null) {
					// connection has been closed
					break;
				}
				System.out.println(inboundEvent.getName() + "; " + inboundEvent.readData(String.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}