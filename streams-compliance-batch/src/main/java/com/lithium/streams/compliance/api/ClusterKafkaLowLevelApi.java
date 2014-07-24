package com.lithium.streams.compliance.api;

import java.util.List;

import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.consumer.SimpleConsumer;

public interface ClusterKafkaLowLevelApi extends KafkaLowLevelApi {

	/**
	 * Find Leader of Brokers
	 */
	public abstract PartitionMetadata findLeader(List<String> brokersList, int port, String topic, int partition);;

	/**
	 * Get Last Offset 
	 */
	public abstract long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName);

	public abstract void process(long a_maxReads, String a_topic, int a_partition, List<String> a_seedBrokers,
			int a_port) throws Exception;

}
