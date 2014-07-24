package com.lithium.streams.compliance.consumer;

import com.codahale.metrics.health.HealthCheck;
import com.lithium.streams.compliance.api.ComplianceBatchMetrics;

public class KafkaConsumerHealthCheck extends HealthCheck {

	private final ComplianceBatchMetrics complianceBatchMetrics;

	public KafkaConsumerHealthCheck(final ComplianceBatchMetrics complianceBatchMetrics) {
		this.complianceBatchMetrics = complianceBatchMetrics;
	}

	@Override
	protected Result check() throws Exception {
		return complianceBatchMetrics.isRunning() ? Result.healthy("Consuming Messages from Kafka") : Result
				.unhealthy("Consumer Idle Or Reader not Registered");
	}

}
