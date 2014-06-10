package com.lithium.streams.compliance.server;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ConnectorStatistics;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.PingServlet;
import com.codahale.metrics.servlets.ThreadDumpServlet;
import com.lithium.streams.compliance.service.ComplianceService;
import com.lithium.streams.compliance.util.DumpServlet;

public class MinimalServer {

	private static final Logger log = LoggerFactory.getLogger(MinimalServer.class);
	private static final MetricRegistry registry = new MetricRegistry();
	private static final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
	private static final ExecutorService threadPoolExecutors = Executors.newCachedThreadPool();
	private static final ServletHolder rsServletHolder = new ServletHolder(ServletContainer.class);
	private static final ResourceConfig resourceConfig = new ResourceConfig();

	public MinimalServer() {
		final JmxReporter reporter = JmxReporter.forRegistry(registry).build();
		reporter.start();

	}

	public static void main(String args[]) throws Exception {
		rsServletHolder.setInitParameter("jersey.config.server.provider.packages", "org.streams.compliance.service");//Set the package where the services reside
		resourceConfig.packages(ComplianceService.class.getPackage().getName());
		resourceConfig.register(JacksonFeature.class);
		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		ServletHolder sh = new ServletHolder(servletContainer);

		QueuedThreadPool threadPool = new QueuedThreadPool(200, 20, 30000);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		// Add REST Servlet
		context.addServlet(sh, "/v1/*");

		//CodeHale param
		context.setAttribute("com.codahale.metrics.servlets.MetricsServlet.registry", registry);
		context.setAttribute("com.codahale.metrics.servlets.HealthCheckServlet.registry", healthCheckRegistry);
		context.setInitParameter("com.codahale.metrics.servlets.MetricsServlet.allowedOrigin", "*");
		context.setAttribute("com.codahale.metrics.servlets.HealthCheckServlet.executor", threadPoolExecutors);

		// Add servlets
		context.addServlet(new ServletHolder(new ComplianceStreamingServlet()), "/stream/*");
		context.addServlet(new ServletHolder(new ComplainceBatchServlet()), "/batch/*");
		context.addServlet(new ServletHolder(new DumpServlet()), "/dump/*");
		context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics/*");
		context.addServlet(new ServletHolder(new AdminServlet()), "/admin/*");
		context.addServlet(new ServletHolder(new HealthCheckServlet()), "/health/*");
		context.addServlet(new ServletHolder(new PingServlet()), "/ping/*");
		context.addServlet(new ServletHolder(new ThreadDumpServlet()), "/tdump/*");

		// Setup Spring context
		context.addEventListener(new ContextLoaderListener());
		context.setInitParameter("contextConfigLocation", "classpath*:/spring/appContext.xml");
		log.info("Spring Context: " + context.getDisplayName());

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
