package com.lithium.streams.compliance.service;

import java.util.List;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 
 * @author vijay.polsani
 *
 * @param <K>
 * @param <V>
 * NOTES:1) In the current implementation. Keep PARTITION to 1 for simplicity. Partitions are for categorizing
 * 		  a Queue types so as to make concurrent access easy. We dont have that req. here
 * 		 2) We need ACK. "request.required.acks", "1"	
 */
public interface KafkaProducer<K, V> {

	/**	Set ProducerConfig
	*	compression
	*	sync vs async production
	*	batch size (for async producers)
	*/
	public void setConfig(ProducerConfig config);

	/**
	 * Sends the data to a single topic, partitioned by key, using either the
	 * synchronous or the asynchronous producer
	 * @param message the producer data object that encapsulates the topic, key and message data
	 * @param sync the producer would like to send SYNC requests.
	 */
	public void send(KeyedMessage<K, V> message, boolean sync);

	/**
	 * Sends the data to a single topic, partitioned by key, using either the
	 * synchronous or the asynchronous producer
	 * @param message the producer data object that encapsulates the topic, key and message data
	 * @param sync the producer would like to send ASYNC requests.
	 * @param batchSize the producer would like to know the Batch size. (queue.enqueue.timeout.ms = -1)
	 */
	public void send(KeyedMessage<K, V> message, boolean async, int batchSize);

	/**
	 * Use this API to send data to multiple topics
	 * @param messages list of producer data objects that encapsulate the topic, key and message data
	 */
	public void send(List<KeyedMessage<K, V>> messages);

	/**
	 * Close API to close the producer pool connections to all Kafka brokers.
	 */
	public void close();
}
