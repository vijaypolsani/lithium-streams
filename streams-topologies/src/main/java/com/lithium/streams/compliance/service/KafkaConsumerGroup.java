package com.lithium.streams.compliance.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;

import com.lithium.streams.compliance.model.Event;

public class KafkaConsumerGroup implements Serializable {

	private static final long serialVersionUID = -9016436840583545187L;
	private static final String TOPIC = "lia";
	private final kafka.javaapi.consumer.ConsumerConnector consumer;
	private final int numberOfThreads = 1;
	private ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
	private KafkaConsumerCallable consumerCallable;
	private Event lock;
	private final String hostNameUrl;
	private final String groupId;
	private final String zkTimeout;
	private final SpoutOutputCollector collector;
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerGroup.class);

	public KafkaConsumerGroup(String hostNameUrl, String groupId, String zkTimeout, SpoutOutputCollector collector)
			throws InterruptedException, ExecutionException {
		this.hostNameUrl = hostNameUrl;
		this.groupId = groupId;
		this.zkTimeout = zkTimeout;
		this.collector = collector;
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
		createThreadPool();
	}

	public Event getLock() {
		return lock;
	}

	private ConsumerConfig createConsumerConfig() {
		Properties props = new Properties();
		props.put("zookeeper.connect", hostNameUrl);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", zkTimeout);
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "500");
		return new ConsumerConfig(props);
	}

	public void createThreadPool() throws InterruptedException, ExecutionException {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(TOPIC, new Integer(numberOfThreads));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(TOPIC);
		// now create an object to consume the messages
		int threadNumber = 1;
		for (KafkaStream<byte[], byte[]> stream : streams) {
			lock = new Event();
			log.info(">>> Calling ThreadNumber : " + threadNumber);
			consumerCallable = new KafkaConsumerCallable(stream, threadNumber, lock, collector);
			//Works good only for 1 thread pool. Check this later after DEMO
			executor.submit(consumerCallable);
			threadNumber++;
		}
	}

	public void shutdown() {
		if (consumer != null)
			consumer.shutdown();
		if (executor != null)
			executor.shutdown();
		log.info("ThreadGroup shutdown complete");
	}
}
