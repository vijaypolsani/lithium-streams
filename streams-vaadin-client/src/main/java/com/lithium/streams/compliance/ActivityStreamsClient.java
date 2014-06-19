package com.lithium.streams.compliance;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.LiaStreamsConsumer.KeepListening;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("streams")
@SuppressWarnings("serial")
public class ActivityStreamsClient extends UI {
	private static final String STREAMS_URL = "http://localhost:6060/compliance/live/actiance.qa?login=vijay";
	//private static final String STREAMS_URL = "http://10.240.180.18:6060/compliance/live/actiance.qa?login=demo";
	private static final Logger log = LoggerFactory.getLogger(ActivityStreamsClient.class);

	private Label timeLabel = new Label("Loading UI, please wait...");
	private Label eventData = new Label("Loading Event Data, Checking Server...");
	private VerticalLayout layout;

	@WebServlet(value = "/*", asyncSupported = true, loadOnStartup = 1)
	@VaadinServletConfiguration(productionMode = false, ui = ActivityStreamsClient.class, widgetset = "com.lithium.streams.compliance.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		//REST Consumer.
		Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
		WebTarget target = client.target(STREAMS_URL);
		log.info(">>> Prepared Target: " + target.getUri());
		final EventSource eventSource = new EventSource(target);
		log.info(">>> Registered Endpoint & Open. Waiting for events...");
		eventSource.register(new KeepListening());

		//UI Layout
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		timeLabel.setSizeUndefined();
		eventData.setSizeUndefined();
		layout.addComponent(timeLabel);
		layout.addComponent(eventData);
		getPushConfiguration().setPushMode(PushMode.AUTOMATIC);

		// Make sure the number of threads are attached to the client.
		new Thread(new EndlessRefresherRunnable(eventSource)).start();
	}

	private class EndlessRefresherRunnable implements Runnable {
		int i = 0;
		int j = 0;

		private EndlessRefresherRunnable(EventSource eventSource) {
			eventSource.register(new KeepListening());
		}

		@Override
		public void run() {
			while (true) {
				try {
					//A 2 Sec sleep to get a fair view of the data.
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				access(new LabelUpdateRunnable());
			}
		}

		final class LabelUpdateRunnable implements Runnable {
			@Override
			public void run() {
				timeLabel.setValue(" Current Time: " + getCurrentTime());
				log.info(">>> Iteration of the page Regresh: " + j++ + " Time: " + getCurrentTime());
				push();
			}

			protected String getCurrentTime() {
				Date date = Calendar.getInstance().getTime();
				return date.toString();
			}
		}

		final class KeepListening implements EventListener {
			@Override
			public void onEvent(InboundEvent inboundEvent) {
				try {
					eventData.setValue(inboundEvent.readData());
					log.info(">>> Data received from REST Server. Iteration times: " + i++);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
