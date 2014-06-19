package com.lithium.streams;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("streams")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

	private Label timeLabel = new Label("Loading UI, please wait...");
	private VerticalLayout layout;

	@WebServlet(value = "/*", asyncSupported = true, loadOnStartup = 1)
	@VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "com.lithium.streams.AppWidgetSet")
	public static class Servlet extends VaadinServlet {
	}

	int j = 0;

	@Override
	protected void init(VaadinRequest request) {
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setContent(layout);
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		timeLabel.setSizeUndefined();
		layout.addComponent(timeLabel);
		getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		new Thread(new EndlessRefresherRunnable()).start();
	}

	private class EndlessRefresherRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				access(new LabelUpdateRunnable());
			}
		}

		final class LabelUpdateRunnable implements Runnable {

			protected String getCurrentTime() {
				Date date = Calendar.getInstance().getTime();
				return date.toString();
			}

			@Override
			public void run() {
				System.out.println("Before this LabelUpdateRunnable: " + j++);
				timeLabel.setValue(" Current Time: " + getCurrentTime());
				System.out.println("After this LabelUpdateRunnable: " + j + getCurrentTime());
				push();
			}
		}
	}

}
