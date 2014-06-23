package com.lithium.streams.compliance.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;

@WebServlet(name = "streams", urlPatterns = { "/streams" }, asyncSupported = true)
public class ActivityStreamsWebClient extends HttpServlet {

	private static final long serialVersionUID = 4566982899630258503L;
	private static String TARGET_URI = "http://localhost:8080/streams-archive-web/lia/streams/sathi.qa";
	private static String PARAM_URI = "http://localhost:8080/streams-archive-web/lia/streams/";

	@Override
	public void init() throws ServletException {
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		response.setContentType("text/html;charset=UTF-8");
		try {
			String community = request.getParameter("community");
			if (community != null) {
				TARGET_URI = PARAM_URI + community;
				System.out.println("URI : " + TARGET_URI);
			}
			final AsyncContext asyncContext = request.startAsync();
			asyncContext.setTimeout(600000);
			asyncContext.addListener(new AsyncListener() {

				public void onComplete(AsyncEvent arg0) throws IOException {
					System.out.println("onComplete: " + arg0.toString());
				}

				public void onError(AsyncEvent arg0) throws IOException {
					System.out.println("onError: " + arg0.toString());
				}

				public void onStartAsync(AsyncEvent arg0) throws IOException {
					System.out.println("onStartAsync: " + arg0.toString());
				}

				public void onTimeout(AsyncEvent arg0) throws IOException {
					System.out.println("onTimeout: " + arg0.toString());
				}
			});
			Thread t = new Thread(new AsyncRequestProcessor(asyncContext));
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class AsyncRequestProcessor implements Runnable {

		private final AsyncContext context;

		public AsyncRequestProcessor(AsyncContext context) {
			this.context = context;
		}

		public void run() {
			Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
			//Client client = ClientBuilder.newClient();
			//client.configuration().register(OutboundEventWriter.class);
			context.getResponse().setContentType("text/html");
			final javax.ws.rs.client.WebTarget webTarget;
			try {
				final PrintWriter out = context.getResponse().getWriter();
				webTarget = client.target(new URI(TARGET_URI));
				out.println("<html>");
				out.println("<head>");
				out.println("<title>Activity Streams Feed Client</title>");
				out.println("</head>");
				out.println("<body>");
				out.println("<h3 align = 'center' style='color:blue'>");
				out.println("Lia Events as Activity Streams");
				out.println("</h1>");

				EventSource eventSource = new EventSource(webTarget) {
					@Override
					public void onEvent(InboundEvent inboundEvent) {
						out.println("<p>");
						out.println("<br>");
						//		out.println("<script language='text/javascript' src='FormatJson.js'>");
						//		out.println("var formattedJson = FormatJSON(");
						out.println("" + inboundEvent.readData());
						//		out.println(")");
						//out.println("</script>");
						out.println("<br><HR COLOR='green' WIDTH='100%'>");
						out.println("</p>");
						out.flush();
					}
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
