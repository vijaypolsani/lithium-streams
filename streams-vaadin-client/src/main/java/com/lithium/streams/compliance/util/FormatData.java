package com.lithium.streams.compliance.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lithium.streams.compliance.model.ActivityStreamv1;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnResizeEvent;

public class FormatData {
	private final static Calendar cal = Calendar.getInstance();
	private static final DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

	public static HorizontalLayout processData(HorizontalLayout layout, ActivityStreamv1 activityStreams, long counter) {
		layout.removeAllComponents();
		final Table table = new Table(" Events: [ " + counter + " ]    Last Refresh: [ " + getCurrentTime() + " ]");
		table.setStyleName("title");
		table.removeAllItems();
		table.addContainerProperty("Activity Streams Property", String.class, null);
		table.addContainerProperty("Streaming Event Value", String.class, null);

		table.addItem(new Object[] { "version", activityStreams.getFormatVersion() }, new Integer(1));
		if (activityStreams.getEvent().getPublished() != null && activityStreams.getEvent().getPublished() != "") {
			cal.setTimeInMillis(Long.parseLong(activityStreams.getEvent().getPublished()));
			table.addItem(new Object[] { "published", (formatter.format(cal.getTime())) }, new Integer(2));
		} else
			table.addItem(new Object[] { "published", (activityStreams.getEvent().getPublished()) }, new Integer(2));
		table.addItem(new Object[] { "title", activityStreams.getEvent().getService() }, new Integer(3));
		table.addItem(new Object[] { "verb", activityStreams.getEvent().getVerb() }, new Integer(4));
		table.addItem(new Object[] { "frameId", activityStreams.getEvent().getFrameId() }, new Integer(5));
		table.addItem(new Object[] { "version", activityStreams.getEvent().getVersion() }, new Integer(6));
		table.addItem(new Object[] { "node", activityStreams.getEvent().getNode() }, new Integer(7));
		table.addItem(new Object[] { "stats", activityStreams.getPayload().getLine() }, new Integer(8));

		//Generator
		table.addItem(new Object[] { "Generator: source", activityStreams.getGenerator().getSource() }, new Integer(9));
		table.addItem(new Object[] { "Generator:eventId", activityStreams.getGenerator().getEventId() },
				new Integer(10));

		//Provider
		table.addItem(new Object[] { "Provider: service", activityStreams.getProvider().getService() }, new Integer(11));
		table.addItem(new Object[] { "Provider: version", activityStreams.getProvider().getVersion() }, new Integer(12));

		//Actor
		table.addItem(new Object[] { "Actor: id", activityStreams.getActor().getUid() }, new Integer(13));
		table.addItem(new Object[] { "Actor: login", activityStreams.getActor().getLogin() }, new Integer(14));
		table.addItem(new Object[] { "Actor: email", activityStreams.getActor().getEmail() }, new Integer(15));
		table.addItem(new Object[] { "Actor: uid", activityStreams.getActor().getUid() }, new Integer(16));
		table.addItem(new Object[] { "Actor: type", activityStreams.getActor().getType() }, new Integer(17));
		table.addItem(new Object[] { "Actor: registrationStatus", activityStreams.getActor().getRegistrationStatus() },
				new Integer(18));
		table.addItem(new Object[] { "Actor: registrationTime", activityStreams.getActor().getRegistrationTime() },
				new Integer(19));

		//StreamObject
		table.addItem(new Object[] { "StreamObject: objectType", activityStreams.getStreamObject().getObjectType() },
				new Integer(20));
		table.addItem(new Object[] { "StreamObject: id", activityStreams.getStreamObject().getId() }, new Integer(21));
		table.addItem(new Object[] { "StreamObject: displayName", activityStreams.getStreamObject().getDisplayName() },
				new Integer(22));
		table.addItem(new Object[] { "StreamObject: content", activityStreams.getStreamObject().getContent() },
				new Integer(23));
		table.addItem(new Object[] { "StreamObject: visibility", activityStreams.getStreamObject().getVisibility() },
				new Integer(24));
		table.addItem(new Object[] { "StreamObject: subject", activityStreams.getStreamObject().getSubject() },
				new Integer(25));
		table.addItem(new Object[] { "StreamObject: added", activityStreams.getStreamObject().getAdded() },
				new Integer(26));

		//Target
		table.addItem(new Object[] { "Target: type", activityStreams.getTarget().getType() }, new Integer(27));
		table.addItem(new Object[] { "Target: conversationType", activityStreams.getTarget().getConversationType() },
				new Integer(28));
		table.addItem(new Object[] { "Target: id", activityStreams.getTarget().getId() }, new Integer(29));
		table.addItem(new Object[] { "Target: conversationId", activityStreams.getTarget().getConversationId() },
				new Integer(30));

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
		return layout;
	}

	private static String getCurrentTime() {
		Date date = Calendar.getInstance().getTime();
		return date.toString();
	}
}
