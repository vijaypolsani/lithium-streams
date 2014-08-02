package com.lithium.streams.compliance.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.api.AbstractComplianceBatchService;
import com.lithium.streams.compliance.api.ComplianceBatchEvents;
import com.lithium.streams.compliance.consumer.KafkaSimpleConsumerFactory;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.util.BatchOperations;

public class ComplianceBatchStandalone extends AbstractComplianceBatchService implements ComplianceBatchEvents {
	private static final Logger log = LoggerFactory.getLogger(ComplianceBatchStandalone.class);

	public ComplianceBatchStandalone(KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory) {
		super(kafkaSimpleConsumerFactory);
		log.info(">>> Called Super for KafkaConnectionPool and Data Discovery.");
	}

	@Override
	public Collection<ComplianceMessage> processStream(String topicName) throws Exception {
		checkNotNull(topicName, "Input Topic/Community name cannot be null.");
		return super.process(new ComplianceContext(topicName, null, null), BatchOperations.ALL);
	}

	@Override
	public Collection<ComplianceMessage> getMessagesFilteredByTime(String topicName, String start, String end)
			throws Exception {
		checkNotNull(topicName, "Input TopicName/CommunityName cannot be null.");
		checkNotNull(start, "Input Start Time cannot be null.");
		checkNotNull(end, "Input Start Time cannot be null.");
		return super.process(new ComplianceContext(topicName, start, end), BatchOperations.FILTER_BY_TIME);
	}

}
