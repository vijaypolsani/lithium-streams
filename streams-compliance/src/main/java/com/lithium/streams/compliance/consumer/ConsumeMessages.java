package com.lithium.streams.compliance.consumer;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lithium.streams.compliance.beans.ConsumeEventsServiceImpl;
import com.lithium.streams.compliance.beans.StreamEventBus;
import com.lithium.streams.compliance.exception.ComplianceServiceException;
import com.lithium.streams.compliance.model.LiaPostEvent;
import com.lithium.streams.compliance.util.StreamEventBusListener;

public class ConsumeMessages extends Thread {
	private ConsumerGroup consumerGroup;
	private final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);
	private final StreamEventBus streamEventBus;
	private final ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"classpath*:/spring/appContext.xml");
	private final StreamEventBusListener streamEventBusListener;

	public ConsumeMessages(String threadName, String hostNameUrl, final String zkTimeout, String topicName,
			String groupId, StreamEventBusListener streamEventBusListener) throws InterruptedException,
			ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId);
		this.streamEventBusListener = streamEventBusListener;
		// Log for Spring Beans.
		streamEventBus = (StreamEventBus) appContext.getBean("streamEventBus");
		log.info(">>> streamEventBus : " + streamEventBus.toString());
		//StreamEventBusHelper streamEventBusHelper = new StreamEventBusHelper();
		streamEventBus.registerSubscriber(streamEventBusListener);

	}

	public void run() {
		//TODO: Publish to Event BUS. The below may not WORK!
		log.info(">>> Created ConsumeMessages thread for reading data from Kafka.");
		try {
			while (true) {
				//TODO: Hide the low level Thread Sync details after Performance. Remove STR and get Direct JSON Content
				synchronized (consumerGroup.getLock()) {
					String str = consumerGroup.getLock().getJsonContent();
					log.debug(">>> In ConsumeMessages Thread. Reading content : " + str);
					if (str != null) {
						log.info(">>> Inside ConsumeMessages sleeping for 1sec.");
						Thread.sleep(1000);
						try {
							streamEventBus.postEvent(new LiaPostEvent(str));
						} catch (ComplianceServiceException cs) {
							streamEventBus.unRegisterSubscriber(streamEventBusListener);
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