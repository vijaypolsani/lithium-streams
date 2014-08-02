package com.lithium.streams.compliance.service;

import java.util.List;

import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.api.AbstractComplianceBatchService;
import com.lithium.streams.compliance.api.ClusterKafkaLowLevelApi;
import com.lithium.streams.compliance.consumer.KafkaSimpleConsumerFactory;

public class ComplianceBatchCluster extends AbstractComplianceBatchService implements ClusterKafkaLowLevelApi {

	private static final Logger log = LoggerFactory.getLogger(ComplianceBatchCluster.class);

	public ComplianceBatchCluster(KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory) {
		super(kafkaSimpleConsumerFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long getLatestOffsetOfTopic(String topicName) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getEarliestOffsetOfTopic(String topicName) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void commitOffsets() {
		// TODO Auto-generated method stub

	}

	@Override
	public TopicMetadataResponse send(TopicMetadataRequest topicMeta) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartitionMetadata findLeader(List<String> brokersList, int port, String topic, int partition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void process(long a_maxReads, String a_topic, int a_partition, List<String> a_seedBrokers, int a_port)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
