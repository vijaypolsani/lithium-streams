package com.lithium.streams.compliance.service;

import java.util.List;
import java.util.Map;

import kafka.consumer.KafkaStream;
import kafka.consumer.TopicFilter;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.Decoder;

public interface KafkaConsumer<V, K> {

	/**
	 * Find Leader of Brokers
	 */
	public PartitionMetadata findLeader(List<String> brokersList, int port, String topic, int partition);;

	/**
	 *  Get a list of valid offsets (up to maxSize) before the given time.
	 *
	 *  @param request a [[kafka.javaapi.OffsetRequest]] object.
	 *  @return OffsetResponse Response data [[kafka.javaapi.OffsetResponse]] object.
	 */
	public kafka.javaapi.OffsetResponse getOffsetsBefore(OffsetRequest offsetRequest);

	/**
	 * Get Last Offset 
	 */
	public long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName);

	/**
	 *  Fetch metadata of a topics.
	 *  @param request TopicMetadataRequest.
	 *  @return TopicMetadataResponse for each topic in the request.
	 */
	public kafka.javaapi.TopicMetadataResponse send(kafka.javaapi.TopicMetadataRequest topicMeta);

	/**
	   *  Fetch a set of messages from a topic.
	   *
	   *  @param fetchRequest specifies the topic name, topic partition, starting byte offset, maximum bytes to be fetched.
	   *  @return FetchResponse Containing a set of fetched messages
	   */
	public FetchResponse fetchAndProcess(kafka.javaapi.FetchRequest fetchRequest);

	/**
	 *  Commit the offsets of all topic/partitions connected by this connector.
	 */
	public void commitOffsets();

	/**
	 * Close the SimpleConsumer.
	 */
	public void close();

	/**
	 *  Shut down the connector
	 */
	public void shutdown();

	void process(long a_maxReads, String a_topic, int a_partition, List<String> a_seedBrokers, int a_port)
			throws Exception;

}
