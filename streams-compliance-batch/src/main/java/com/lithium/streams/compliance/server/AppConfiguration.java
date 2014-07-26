package com.lithium.streams.compliance.server;

import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.DispatcherType;
import javax.ws.rs.ApplicationPath;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.AdminServlet;
import com.lithium.streams.compliance.handler.ComplainceHandlerProcessor;
import com.lithium.streams.compliance.service.ComplianceBatchStandalone;
import com.lithium.streams.compliance.service.ws.ComplianceBatchService;
import com.lithium.streams.compliance.service.ws.ComplianceClientStateService;
import com.lithium.streams.compliance.util.ComplianceServerFilter;
import com.lithium.streams.compliance.util.DumpServlet;
import com.lithium.streams.compliance.util.GzipWriterInterceptor;

@ApplicationPath("compliance")
public class AppConfiguration extends ResourceConfig {
	private static final MetricRegistry registry = new MetricRegistry();
	private static final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
	private static final ExecutorService threadPoolExecutors = Executors.newCachedThreadPool();

	public AppConfiguration() {
		packages(ComplianceBatchService.class.getPackage().getName());
		//Bean Registration
		register(JacksonFeature.class);
		register(com.lithium.streams.compliance.service.StreamCache.class);

		//Note: The 'ComplianceService' registration will stop many thread creation on Kafka.
		register(ComplianceBatchService.class);
		register(ComplianceClientStateService.class);

		//Bean used for Calling from REST Service
		register(ComplianceBatchStandalone.class);
		register(ComplainceHandlerProcessor.class);
		register(com.lithium.streams.compliance.util.GzipWriterInterceptor.class);
		property(ServerProperties.PROVIDER_CLASSNAMES, com.lithium.streams.compliance.util.GzipWriterInterceptor.class);

	}

	public static final ServletContextHandler getContextConfiguration() {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		//CodeHale param
		context.setAttribute("com.codahale.metrics.servlets.MetricsServlet.registry", registry);
		context.setAttribute("com.codahale.metrics.servlets.HealthCheckServlet.registry", healthCheckRegistry);
		context.setInitParameter("com.codahale.metrics.servlets.MetricsServlet.allowedOrigin", "*");
		context.setAttribute("com.codahale.metrics.servlets.HealthCheckServlet.executor", threadPoolExecutors);

		// Setup context listener
		context.addEventListener(new org.springframework.web.context.ContextLoaderListener());
		context.setInitParameter("contextConfigLocation", "classpath*:/spring/appContext.xml");

		// Add REST Servlet
		context.addServlet(new ServletHolder(new ServletContainer(new AppConfiguration())), "/compliance/*");
		// Servlet Filter to convert to ZIP donwload or add more Headers.
		context.addFilter(ComplianceServerFilter.class, "/compliance/*", EnumSet.of(DispatcherType.REQUEST,
				DispatcherType.ASYNC, DispatcherType.FORWARD, DispatcherType.INCLUDE));

		// Add servlets
		context.addServlet(new ServletHolder(new AdminServlet()), "/admin/*");
		context.addServlet(new ServletHolder(new DumpServlet()), "/dump/*");
		return context;
	}
}
