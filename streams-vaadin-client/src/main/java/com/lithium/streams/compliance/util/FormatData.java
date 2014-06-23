package com.lithium.streams.compliance.util;

import java.util.Calendar;
import java.util.Date;

import com.lithium.streams.compliance.model.ActivityStreams;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnResizeEvent;
import com.vaadin.ui.VerticalLayout;

public class FormatData {

	public static HorizontalLayout processData(HorizontalLayout layout, ActivityStreams activityStreams) {
		layout.removeAllComponents();
		final Table table = new Table("Lithium Activity Streams. Last Update Time: " + getCurrentTime());
		table.setStyleName("title");
		table.removeAllItems();
		table.addContainerProperty("Activity Streams Property", String.class, null);
		table.addContainerProperty("Streaming Event Value", String.class, null);

		table.addItem(new Object[] { "version", activityStreams.getVersion() }, new Integer(1));
		table.addItem(new Object[] { "published", activityStreams.getPublished() }, new Integer(2));
		table.addItem(new Object[] { "generator", "MAIN TAG" }, new Integer(3));
		table.addItem(new Object[] { "source", activityStreams.getGenerator().getSource() }, new Integer(4));
		table.addItem(new Object[] { "provider", "MAIN TAG" }, new Integer(5));
		table.addItem(new Object[] { "service", activityStreams.getProvider().getService() }, new Integer(6));
		table.addItem(new Object[] { "extensionElements", activityStreams.getExtensionElements() }, new Integer(7));
		table.addItem(new Object[] { "title", activityStreams.getTitle() }, new Integer(8));
		table.addItem(new Object[] { "actor", "MAIN TAG" }, new Integer(9));
		table.addItem(new Object[] { "id", activityStreams.getActor().getId() }, new Integer(10));
		table.addItem(new Object[] { "login", activityStreams.getActor().getLogin() }, new Integer(11));
		table.addItem(new Object[] { "registrationStatus", activityStreams.getActor().getRegistrationStatus() },
				new Integer(12));
		table.addItem(new Object[] { "email", activityStreams.getActor().getEmail() }, new Integer(13));
		table.addItem(new Object[] { "uid", activityStreams.getActor().getUid() }, new Integer(14));
		table.addItem(new Object[] { "type", activityStreams.getActor().getType() }, new Integer(15));
		table.addItem(new Object[] { "frameId", activityStreams.getActor().getFrameId() }, new Integer(16));
		table.addItem(new Object[] { "typeRanking", activityStreams.getActor().getTypeRanking() }, new Integer(17));
		table.addItem(new Object[] { "model", activityStreams.getActor().getModel() }, new Integer(18));
		table.addItem(new Object[] { "name", activityStreams.getActor().getName() }, new Integer(19));
		table.addItem(new Object[] { "verb", activityStreams.getVerb() }, new Integer(20));
		table.addItem(new Object[] { "obj", "MAIN TAG" }, new Integer(21));
		table.addItem(new Object[] { "actionId", activityStreams.getObj().getActionId() }, new Integer(22));
		table.addItem(new Object[] { "actionKey", activityStreams.getObj().getActionKey() }, new Integer(23));
		table.addItem(new Object[] { "actionResult", activityStreams.getObj().getActionResult() }, new Integer(24));
		table.addItem(new Object[] { "target", "MAIN TAG" }, new Integer(25));
		table.addItem(new Object[] { "version", activityStreams.getTarget().getVersion() }, new Integer(26));
		table.addItem(new Object[] { "type", activityStreams.getTarget().getType() }, new Integer(27));
		table.addItem(new Object[] { "source", activityStreams.getTarget().getSource() }, new Integer(28));
		table.addItem(new Object[] { "service", activityStreams.getTarget().getService() }, new Integer(29));
		table.addItem(new Object[] { "node", activityStreams.getTarget().getNode() }, new Integer(30));
		table.addItem(new Object[] { "num", activityStreams.getTarget().getNum() }, new Integer(31));
		table.addItem(new Object[] { "visibility", activityStreams.getTarget().getVisibility() }, new Integer(32));
		table.addItem(new Object[] { "subject", activityStreams.getTarget().getSubject() }, new Integer(33));
		table.addItem(new Object[] { "post_time", activityStreams.getTarget().getPost_time() }, new Integer(34));
		table.addItem(new Object[] { "conversationStype", activityStreams.getTarget().getConversationStype() },
				new Integer(35));
		table.addItem(new Object[] { "isTopic", activityStreams.getTarget().getIsTopic() }, new Integer(36));
		table.addItem(new Object[] { "message_type", activityStreams.getTarget().getMessage_type() }, new Integer(37));

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
