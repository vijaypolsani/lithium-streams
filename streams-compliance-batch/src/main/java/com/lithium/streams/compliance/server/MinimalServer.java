package com.lithium.streams.compliance.server;

import java.lang.management.ManagementFactory;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.ConnectorStatistics;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

public class MinimalServer {

	private static final Logger log = LoggerFactory.getLogger(MinimalServer.class);
	private static final MetricRegistry registry = new MetricRegistry();
	private static final QueuedThreadPool threadPool = new QueuedThreadPool(200, 20, 30000);
	private static final HttpConfiguration http_config = new HttpConfiguration();
	private static final Connector[] connectors = new Connector[2];
	private static final ComplianceLifeCycleListener complianceLifeCycleListener = new ComplianceLifeCycleListener();
	private static final Server server = new Server(threadPool);
	private static final ServerConnector serverConnector = new ServerConnector(server, new HttpConnectionFactory(
			http_config));
	private static final SslContextFactory sslContextFactory = new SslContextFactory();
	private static final HttpConfiguration https_config = new HttpConfiguration(http_config);
	private static final ServerConnector sslServerConnector = new ServerConnector(server, new SslConnectionFactory(
			sslContextFactory, "http/1.1"), new HttpConnectionFactory(https_config));

	public MinimalServer() {
		final JmxReporter reporter = JmxReporter.forRegistry(registry).build();
		reporter.start();
		PropertyConfigurator.configure("./log4j.properties");
		 
	}

	public static void main(String args[]) throws Exception {
		log.info("Staring Setver.");
		server.addBean(new MBeanContainer(ManagementFactory.getPlatformMBeanServer()));
		ConnectorStatistics.addToAllConnectors(server);

		http_config.setSecureScheme("https");
		serverConnector.setPort(7070);
		serverConnector.setIdleTimeout(108000);
		connectors[0] = serverConnector;

		sslContextFactory.setKeyStorePath("./keystore.jks");
		sslContextFactory.setKeyStorePassword("changeme");
		sslContextFactory.setKeyManagerPassword("changeme");
		https_config.addCustomizer(new SecureRequestCustomizer());
		sslServerConnector.setPort(7443);
		sslServerConnector.setIdleTimeout(108000);
		connectors[1] = sslServerConnector;

		server.addLifeCycleListener(complianceLifeCycleListener);
		server.setHandler(AppConfiguration.getContextConfiguration());
		server.setConnectors(connectors);
		server.start();
		server.join();
	}
}
