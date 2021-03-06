package com.lithium.streams.compliance.util;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

@Configuration
@EnableMetrics
public class SpringMetrics extends MetricsConfigurerAdapter {

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {
		ConsoleReporter.forRegistry(metricRegistry).build().start(1, TimeUnit.MINUTES);
	}

}