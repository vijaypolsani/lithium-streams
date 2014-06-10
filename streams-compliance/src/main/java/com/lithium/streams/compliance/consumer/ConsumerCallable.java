package com.lithium.streams.compliance.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerCallable implements Callable<List<String>> {
	private final KafkaStream<byte[], byte[]> kafkaStream;
	private final int threadNumber;
	private static final Logger log = LoggerFactory.getLogger(ConsumerCallable.class);
	private Event lock;

	public ConsumerCallable(KafkaStream<byte[], byte[]> stream, int threadNumber, Event lock) {
		this.threadNumber = threadNumber;
		kafkaStream = stream;
		this.lock = lock;
	}

	public List<String> call() throws Exception {
		final List<String> jsonContent = new ArrayList<String>();
		ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
		//log.info("In Consumer Callable: " + it.hasNext());
		while (it.hasNext()) {
			synchronized (lock) {
				lock.setJsonContent(new String(it.next().message()));
				//log.info("Thread Number: " + threadNumber + " Message Content: " + lock.getJsonContent());
				jsonContent.add(lock.getJsonContent());
				lock.notifyAll();
			}
		}
		return jsonContent;
	}
}
