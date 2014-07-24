package com.lithium.streams.compliance.service;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.Response;

import com.lithium.streams.compliance.api.AbstractComplianceBatchService;
import com.lithium.streams.compliance.api.ComplianceBatchEvents;
import com.lithium.streams.compliance.consumer.KafkaSimpleConsumerFactory;

public class ComplianceBatchStandalone extends AbstractComplianceBatchService implements ComplianceBatchEvents {

	public ComplianceBatchStandalone(KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory) {
		super(kafkaSimpleConsumerFactory);
	}

	@Override
	public Response getBatchLatestSequenceId(String clientId, String startId, String endId)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getBatchLatestEndTime(String clientId, String startTime, String endTime)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

}
