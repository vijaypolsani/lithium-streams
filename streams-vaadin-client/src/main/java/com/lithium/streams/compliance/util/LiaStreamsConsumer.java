package com.lithium.streams.compliance.util;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiaStreamsConsumer {

	private static final String STREAMS_API_PROXY_URL = "https://qa.lcloud.com/compliance/v1/live";
	//private static final String STREAMS_URL = "http://localhost:6060/compliance/v1/live";
	//private static final String STREAMS_COMPLIANCE_URL = "http://10.240.180.18:6060/compliance/v1/live";

	private static final String CLIENT_TOKEN = "client-id";
	private static final String CLIENT_TOKEN_VALUE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";

	//private static final String STREAMS_URL = "http://localhost:6060/compliance/live";
	private static final Logger log = LoggerFactory.getLogger(LiaStreamsConsumer.class);
	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType) {
		}
	} };

	public static void main(String[] args) {
		SslConfigurator sslConfig = SslConfigurator.newInstance().trustStoreFile(
				"/Library/Java/JavaVirtualMachines/jdk1.7.0_51.jdk/Contents/Home/jre/lib/security/jssecacerts")
				.trustStorePassword("changeit").keyStoreFile("/Users/vijay.polsani/keystore.jks").keyPassword(
						"changeme");

		SSLContext sslContext = sslConfig.createSSLContext();
		Client client = ClientBuilder.newBuilder().register(SseFeature.SERVER_SENT_EVENTS).sslContext(sslContext)
				.build();

		WebTarget webTarget = client.target(UriBuilder.fromPath(STREAMS_API_PROXY_URL));
		Response response = webTarget.request("text/plain").header(CLIENT_TOKEN, CLIENT_TOKEN_VALUE).buildGet().invoke();
		log.info(">>> Response: " + response.getStatus());
		log.info(">>> Response: " + response.getHeaders().toString());
		log.info(">>> Prepared Target: " + webTarget.getUri());
		final EventSource eventSource = new EventSource(webTarget);
		log.info(">>> Registered Endpoint & Open. Waiting.." + eventSource);
		log.info("Response: is Open " + eventSource.isOpen());
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
