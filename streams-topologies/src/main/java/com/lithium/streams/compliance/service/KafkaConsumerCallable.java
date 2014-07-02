package com.lithium.streams.compliance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.tuple.Values;

import com.lithium.streams.compliance.model.Event;

public class KafkaConsumerCallable implements Callable<List<String>> {
	private final KafkaStream<byte[], byte[]> kafkaStream;
	private final int threadNumber;
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerCallable.class);
	private Event lock;
	private SpoutOutputCollector collector;

	public KafkaConsumerCallable(KafkaStream<byte[], byte[]> stream, int threadNumber, Event lock,
			SpoutOutputCollector collector) {
		this.threadNumber = threadNumber;
		kafkaStream = stream;
		this.lock = lock;
		this.collector = collector;
	}

	public List<String> call() throws Exception {
		final List<String> jsonContent = new ArrayList<String>();
		ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
		//** The below call will block the Kafka Thread. DONT do that blocking.. .
		//log.info("In Consumer Callable: " + it.hasNext());
		while (it.hasNext()) {
			synchronized (lock) {
				lock.setJsonContent(new String(it.next().message()));
				jsonContent.add(lock.getJsonContent());
				if (lock.getJsonContent() != null) {
					String uuidTracking = UUID.randomUUID().toString();
					collector.emit(new Values(lock.getJsonContent()), uuidTracking);
					//log.info(">>> *** Emitted nextTuple Thread: " + threadNumber + " Message Content: "+ lock.getJsonContent());
					log.info(">>> *** Emitted nextTuple Thread: " + threadNumber + " Tracking: " + uuidTracking);
				}
				lock.notifyAll();
			}
		}
		return jsonContent;
	}
}
