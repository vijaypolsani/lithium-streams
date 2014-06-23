package com.lithium.streams.compliance.consumer;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lithium.streams.compliance.beans.ConsumeEventsService;
import com.lithium.streams.compliance.beans.ConsumeEventsServiceImpl;
import com.lithium.streams.compliance.beans.StreamEventBus;
import com.lithium.streams.compliance.model.LiaPostEvent;
import com.lithium.streams.compliance.util.StreamEventBusHelper;

public class ConsumeMessages extends Thread {
	private ConsumerGroup consumerGroup;
	private final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);
	protected Deque<String> data = new ConcurrentLinkedDeque<String>();
	private final StreamEventBus streamEventBus;
	private final ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"classpath*:/spring/appContext.xml");

	public ConsumeMessages(String threadName, String hostNameUrl, final String zkTimeout, String topicName,
			String groupId) throws InterruptedException, ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId);
		// Log for Spring Beans.
		streamEventBus = (StreamEventBus) appContext.getBean("streamEventBus");
		log.info(">>> streamEventBus : " + streamEventBus.toString());
		streamEventBus.registerSubscriber(new StreamEventBusHelper());

	}

	public Deque<String> getData() {
		return data;
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
						data.add(str);
						streamEventBus.postEvent(new LiaPostEvent(str));
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