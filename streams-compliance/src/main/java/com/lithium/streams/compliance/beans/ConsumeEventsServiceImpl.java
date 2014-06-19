package com.lithium.streams.compliance.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lithium.streams.compliance.consumer.ConsumerGroup;

public class ConsumeEventsServiceImpl implements ConsumeEventsService {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String GROUP_ID = "LiaConsumer";
	private static final String ZK_TIMEOUT = "5000";

	@Autowired
	private TransformationService transformationService;
	private static final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);

	// Use this Service to WRAP the access to the Kafka Consumer Threads
	public ConsumeEventsServiceImpl() throws InterruptedException, ExecutionException {
	}

	@Override
	public List<String> consumeLiaActivitySteamsEvents(String communityName, String login) {
		ConsumeMessages consumeMessages = null;
		// Make sure to take a Login ID for Attaching a ConsumerGroup in Future.
		if (login == null)
			login = GROUP_ID;
		try {
			consumeMessages = new ConsumeMessages("Lia_RAW_ConsumeThread", ZK_HOSTNAME_URL, GROUP_ID, ZK_TIMEOUT);
			consumeMessages.start();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		return consumeMessages.getData();
	}

	@Override
	public List<String> consumeLiaActivitySteamsEvents(String communityName, String login, String user) {
		ConsumeMessages consumeMessages = null;
		// Make sure to take a Login ID for Attaching a ConsumerGroup in Future.
		if (login == null)
			login = GROUP_ID;
		try {
			consumeMessages = new ConsumeMessages("Lia_AS1.0_ConsumeThread", ZK_HOSTNAME_URL, GROUP_ID, ZK_TIMEOUT);
			consumeMessages.start();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		return consumeMessages.getData();
	}
}

class ConsumeMessages extends Thread {
	private ConsumerGroup consumerGroup;
	private static final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);
	private final List<String> data = new ArrayList<String>();

	public List<String> getData() {
		return data;
	}

	public ConsumeMessages(String threadName, String hostNameUrl, String groupId, final String zkTimeout)
			throws InterruptedException, ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, groupId, zkTimeout);
	}

	public void run() {
		try {
			while (true) {
				// Hide the low level Thread Sync details after DEMO
				synchronized (consumerGroup.getLock()) {
					log.info(">>> In ConsumeMessages Thread. Reading content : "
							+ consumerGroup.getLock().getJsonContent());
					data.add(consumerGroup.getLock().getJsonContent());
					consumerGroup.getLock().wait();
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
