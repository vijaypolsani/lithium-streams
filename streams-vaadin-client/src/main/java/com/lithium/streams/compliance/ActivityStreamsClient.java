package com.lithium.streams.compliance;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.util.FormatData;
import com.lithium.streams.compliance.util.JsonMessageParser;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Theme("streams")
//@Theme("runo")
@SuppressWarnings("serial")
public class ActivityStreamsClient extends UI {

	//private static final String STREAMS_API_PROXY_URL = "https://qa.lcloud.com/compliance/v1/live";
	//private static final String STREAMS_URL = "http://localhost:6060/compliance/v1/live";
	private static final String STREAMS_COMPLIANCE_URL = "http://10.240.180.18:6060/compliance/v1/live";

	private static final String CLIENT_TOKEN = "client-id";
	private static final String CLIENT_TOKEN_VALUE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";
	private static final Logger log = LoggerFactory.getLogger(ActivityStreamsClient.class);

	private Label timeLabel = new Label("Loading UI, please wait...");
	private Label eventData = new Label("Loading Event Data, Checking Server...");
	private HorizontalLayout layout;

	protected Deque<String> queue = new ConcurrentLinkedDeque<String>();
	private javax.ws.rs.client.WebTarget webTarget;

	@WebServlet(value = "/*", asyncSupported = true, loadOnStartup = 1)
	@VaadinServletConfiguration(productionMode = false, ui = ActivityStreamsClient.class, widgetset = "com.lithium.streams.compliance.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		//UI Layout
		layout = new HorizontalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		timeLabel.setSizeUndefined();
		eventData.setSizeUndefined();
		layout.addComponent(timeLabel);
		layout.addComponent(eventData);
		getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
		webTarget = client.target(UriBuilder.fromPath(STREAMS_COMPLIANCE_URL));
		webTarget.request("text/plain").header(CLIENT_TOKEN, CLIENT_TOKEN_VALUE).buildGet();
		System.out.println("URL: " + webTarget.getUri());
		Thread ui = new Thread(new EndlessRefresherRunnable());
		ui.start();

		Thread et = new Thread(new AsyncRequestProcessor());
		et.start();
	}

	private class EndlessRefresherRunnable implements Runnable {
		LabelUpdateRunnable labelUpdateRunnable = new LabelUpdateRunnable();

		@Override
		public void run() {
			while (true) {
				try {
					//Sleep for 1 sec.s
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					access(labelUpdateRunnable);
				} catch (com.vaadin.ui.UIDetachedException ui) {
					log.error("<<< Exception in closing UI. Check Vaadin latest version. " + ui.getLocalizedMessage());
				}
			}
		}

		final class LabelUpdateRunnable implements Runnable {
			long counter = 0L;

			@Override
			public void run() {
				timeLabel.setValue(" Current Time: " + getCurrentTime());
				log.info(">>> Iteration of the page Regresh: " + ++counter + " Time: " + getCurrentTime());
				if (queue.peek() != null) {
					String data = queue.pop();
					try {
						layout = FormatData.processData(layout, JsonMessageParser
								.parseIncomingsJsonStreamToObject(data), counter);
						layout.setCaption("Lithium Compliance Service Real Time Events");
						layout.setMargin(true);
						layout.setSpacing(true);
						layout.addComponent(new Label(data));
						//layout.addComponent(new Label(getCurrentTime()));
						layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
					} catch (IOException e) {
						e.printStackTrace();
					}
					eventData.setValue(data);
				}
				push();
			}

			protected String getCurrentTime() {
				Date date = Calendar.getInstance().getTime();
				return date.toString();
			}
		}
	}

	class AsyncRequestProcessor implements Runnable {
		int i = 0;

		public AsyncRequestProcessor() {
			super();
			log.info(">>> AsyncRequestProcessor Created for reading Events. " + this.toString());
		}

		public void run() {
			try {
				EventSource eventSource = new EventSource(webTarget) {
					@Override
					public void onEvent(InboundEvent inboundEvent) {
						log.info(">>> Created for Target . onEvent: " + i);
						String inboundEventData = inboundEvent.readData();
						if (inboundEventData != null) {
							queue.add(inboundEventData);
							log.info(">>> Event iteration: " + i++ + " Data: " + inboundEventData);
						} else {
							log.error(">>> Event iteration: " + i++ + " - Connection is closed. ");

						}
					}
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
