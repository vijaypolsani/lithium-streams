package com.lithium.streams.compliance.consumer;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId);
		this.streamEventBus = streamEventBus;
	}

	public void run() {
		//TODO: Publish to Event BUS. The below may not WORK!
		log.info(">>> Created ConsumeMessages thread for reading data from Kafka.");
		try {
			while (true) {
				//TODO: Hide the low level Thread Sync details after Performance. Remove STR and get Direct JSON Content
				synchronized (consumerGroup.getLock()) {
					String str = consumerGroup.getLock().getJsonContent();
					log.info(">>> In ConsumeMessages Thread. Reading content : " + str);
					if (str != null) {
						log.info(">>> Inside ConsumeMessages sleeping for 1sec. ");
						Thread.sleep(1000);
						try {
							streamEventBus.postEvent(new LiaPostEvent(str));
						} catch (ComplianceServiceException cs) {
							throw new ComplianceServiceException("LI002", "Unregistred the listener. Reregister.", cs);
						}
					}
					consumerGroup.getLock().wait();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}