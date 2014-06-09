package org.streams.compliance.server;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ConnectorStatistics;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.annotation.Name;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.springframework.web.context.ContextLoaderListener;

import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.servlets.MetricsServletsContextListener;

public class MinimalServer {

	public MinimalServer() {
		final MetricRegistry metrics = new MetricRegistry();
	}

	public static void main(String args[]) throws Exception {

		QueuedThreadPool threadPool = new QueuedThreadPool(200, 20, 30000);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		// Add servlets
		context.addServlet(new ServletHolder(new ComplianceStreamingServlet()), "/stream/*");
		context.addServlet(new ServletHolder(new ComplainceBatchServlet()), "/batch/*");
		context.addServlet(new ServletHolder(new DumpServlet()), "/dump/*");

		// Setup Spring context
		context.addEventListener(new ContextLoaderListener());
		context.setInitParameter("contextConfigLocation", "classpath*:**/appContext.xml");

		context.addEventListener(new MetricsServletsContextListener());
		Server server = new Server(threadPool);
		//Server server = new Server();
		server.addBean(new MBeanContainer(ManagementFactory.getPlatformMBeanServer()));
		ConnectorStatistics.addToAllConnectors(server);

		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(8443);
		http_config.setOutputBufferSize(32768);

		Connector[] connectors = new Connector[2];
		connectors[0] = setHttpConfiguration(server, http_config);
		connectors[1] = setHttpsConfiguration(server, http_config);

		ComplianceLifeCycleListener complianceListener = new ComplianceLifeCycleListener();
		server.addLifeCycleListener(complianceListener);

		server.setHandler(context);
		server.setConnectors(connectors);
		server.start();
		server.join();

	}

	private static ServerConnector setHttpConfiguration(Server server, HttpConfiguration http_config) {
		ServerConnector serverConnector = new ServerConnector(server, new HttpConnectionFactory(http_config));
		serverConnector.setPort(8080);
		serverConnector.setIdleTimeout(108000);
		return serverConnector;
	}

	private static ServerConnector setHttpsConfiguration(Server server, HttpConfiguration http_config) {
		SslContextFactory sslContextFactory = new SslContextFactory();
		System.out.println("user.home " + System.getProperty("user.home"));
		sslContextFactory.setKeyStorePath(System.getProperty("user.home") + "/keystore.jks");
		sslContextFactory.setKeyStorePassword("changeme");
		sslContextFactory.setKeyManagerPassword("changeme");

		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());
		ServerConnector serverConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory,
				"http/1.1"), new HttpConnectionFactory(https_config));
		serverConnector.setPort(8443);
		serverConnector.setIdleTimeout(108000);
		return serverConnector;
	}
}
