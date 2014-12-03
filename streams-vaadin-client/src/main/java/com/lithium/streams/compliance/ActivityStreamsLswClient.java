package com.lithium.streams.compliance;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.util.FormatData;
import com.lithium.streams.compliance.util.JsonMessageParser;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnResizeEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("streams")
//@Theme("runo")
@SuppressWarnings("serial")
public class ActivityStreamsLswClient extends UI {

	//private static final String STREAMS_API_PROXY_URL = "https://qa.lcloud.com/compliance/v1/live/lsw";
	//private static final String STREAMS_URL = "http://localhost:6060/compliance/v1/live/lsw";
	private static final String STREAMS_COMPLIANCE_URL = "http://10.220.77.243:6060/compliance/v1/live/lsw";

	private static final String CLIENT_TOKEN = "client-id";
	private static final String CLIENT_TOKEN_VALUE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";
	private static final Logger log = LoggerFactory.getLogger(ActivityStreamsLswClient.class);

	private Label timeLabel = new Label("Loading UI, please wait...");
	private Label eventData = new Label("Loading Event Data, Checking Server...");
	//private HorizontalLayout layout;
	private VerticalLayout layout;

	protected Deque<String> queue = new ConcurrentLinkedDeque<String>();
	private static final Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
	private static final javax.ws.rs.client.WebTarget webTarget = client.target(UriBuilder
			.fromPath(STREAMS_COMPLIANCE_URL));
	private static final Invocation invocation = webTarget.request().header(CLIENT_TOKEN, CLIENT_TOKEN_VALUE)
			.buildGet();

	@WebServlet(value = "/lsw/*", asyncSupported = true, loadOnStartup = 1)
	@VaadinServletConfiguration(productionMode = false, ui = ActivityStreamsLswClient.class, widgetset = "com.lithium.streams.compliance.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		//UI Layout
		//layout = new HorizontalLayout();
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
					//e.printStackTrace();
				}
				try {
					access(labelUpdateRunnable);
				} catch (com.vaadin.ui.UIDetachedException ui) {
					log.error("<<< Exception in closing UI. Check Vaadin latest version. " + ui.getLocalizedMessage());
					break;
				}
			}
		}

		final class LabelUpdateRunnable implements Runnable {
			long counter = 0L;

			@Override
			public void run() {
				timeLabel.setValue(" Current Time: " + getCurrentTime());
				if (queue.peek() != null) {
					System.out.println(">>> Iteration of the data: " + ++counter + " Time: " + getCurrentTime());
					layout.removeAllComponents();
					final Table table = new Table(" Events: [ " + counter + " ]    Last Refresh: [ " + getCurrentTime()
							+ " ]");
					table.setStyleName("title");
					table.removeAllItems();
					table.addContainerProperty("Activity Streams LSW", String.class, null);
					String data = queue.pop();
					//System.out.println(">>> data: " + data);

					table.addItem(new Object[] { data }, new Integer(1));
					table.addColumnResizeListener(new Table.ColumnResizeListener() {
						public void columnResize(ColumnResizeEvent event) {
							// Get the new width of the resized column
							int width = event.getCurrentWidth();

							// Get the property ID of the resized column
							String column = (String) event.getPropertyId();

							// Do something with the information
							table.setColumnFooter(column, String.valueOf(width) + "px");
						}
					});

					// Must be immediate to send the resize events immediately
					table.setImmediate(true);
					// Adjust the table height a bit
					table.setPageLength(table.size());
					layout.addComponent(table);

					layout.setCaption("Lithium Compliance Service Real Time Events");
					layout.setMargin(true);
					layout.setSpacing(true);
					layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
					layout.addComponent(eventData);
					eventData.setValue(data);
					layout.setVisible(true);
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
				log.info(">>> Created for Client Web Target. ");
				EventInput eventInput = invocation.invoke(EventInput.class);
				while (!eventInput.isClosed()) {
					final InboundEvent inboundEvent = eventInput.read();
					if (inboundEvent != null) {
						log.info(">>> Consumer received data for the connection: " + Thread.currentThread().getName());
						queue.add(inboundEvent.readData(String.class));
						log.info(">>> Event iteration: " + ++i + " Name: " + inboundEvent.getName() + "Data: "
								+ inboundEvent.readData(String.class));
					} else {
						log.error(">>> Event iteration: " + i++ + " - Connection is closed. ");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void forEach(Consumer<? super Component> action) {
		// TODO Auto-generated method stub

	}

	@Override
	public Spliterator<Component> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
