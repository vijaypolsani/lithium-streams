package com.lithium.streams.compliance.consumer;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.beans.ConsumeEventsServiceImpl;
import com.lithium.streams.compliance.beans.StreamEventBus;
import com.lithium.streams.compliance.exception.ComplianceServiceException;
import com.lithium.streams.compliance.model.LiaPostEvent;

public class ConsumeMessages extends Thread {
	private final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);
	private final ConsumerGroup consumerGroup;
	private final StreamEventBus streamEventBus;

	public ConsumeMessages(String threadName, String hostNameUrl, final String zkTimeout, String topicName,
			String groupId, StreamEventBus streamEventBus) throws InterruptedException, ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId, streamEventBus);
		this.streamEventBus = streamEventBus;
	}

	public void run() {
		long counter = 0L;
		log.info(">>> Created ConsumeMessages thread for reading data from Kafka.");
		while (true) {
			//TODO: Hide the low level Thread Sync details after Performance. Remove STR and get Direct JSON Content
			synchronized (consumerGroup.getLock()) {
				log.info(">>> ConsumeMessages Handler Thread. *CC = " + counter++ + " * Data: "
						+ consumerGroup.getLock().getJsonContent());
				if (consumerGroup.getLock().getJsonContent() != null) {
					try {
						streamEventBus
								.postEvent(new LiaPostEvent(new String(consumerGroup.getLock().getJsonContent())));
					} catch (ComplianceServiceException cs) {
						throw new ComplianceServiceException("LI002", "Unregistred the listener. Reregister.", cs);
					}
					consumerGroup.getLock().notifyAll();
				}
				try {
					log.info(">>> In Wait ConsumeMessages.");
					consumerGroup.getLock().wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}