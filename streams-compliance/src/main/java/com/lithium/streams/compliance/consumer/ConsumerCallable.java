package com.lithium.streams.compliance.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import lithium.research.key.KeySource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lithium.streams.compliance.beans.StreamEventBus;
import com.lithium.streams.compliance.client.IDecryption;
import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.KeyServerProperties;
import com.lithium.streams.compliance.util.KeySourceUtil;

public class ConsumerCallable implements Callable<List<byte[]>> {
	private final KafkaStream<byte[], byte[]> kafkaStream;
	private final int threadNumber;
	private static final Logger log = LoggerFactory.getLogger(ConsumerCallable.class);
	private Event lock;
	private final StreamEventBus streamEventBus;

	public ConsumerCallable(KafkaStream<byte[], byte[]> stream, int threadNumber, Event lock,
			StreamEventBus streamEventBus) {
		this.threadNumber = threadNumber;
		kafkaStream = stream;
		this.lock = lock;
		this.streamEventBus = streamEventBus;
	}

	public List<byte[]> call() throws Exception {
		long counter = 0L;
		final List<byte[]> jsonContent = new ArrayList<byte[]>();
		ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
		//log.info("In Consumer Callable: " + it.hasNext());
		while (it.hasNext()) {
			synchronized (lock) {
				lock.setJsonContent(it.next().message());
				log.info("Kafka Reader Thread Number: " + threadNumber + " *KC: =" + ++counter);
				lock.notifyAll();
				if (lock.getJsonContent() != null) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lock.setJsonContent(null);
			}
		}
		return jsonContent;
	}
}
