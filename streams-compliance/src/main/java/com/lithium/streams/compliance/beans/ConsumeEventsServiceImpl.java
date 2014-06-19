package com.lithium.streams.compliance.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.base.Preconditions.*;

import com.lithium.streams.compliance.consumer.ConsumerGroup;

public class ConsumeEventsServiceImpl implements ConsumeEventsService {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String ZK_TIMEOUT = "5000";

	@Autowired
	private TransformationService transformationService;
	private static final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);

	// Use this Service to WRAP the access to the Kafka Consumer Threads
	public ConsumeEventsServiceImpl() throws InterruptedException, ExecutionException {
	}

	@Override
	public List<String> consumeEvents(String communityName, String login) {
		checkNotNull(communityName, "Community Name parameter cannot be NULL.");
		checkNotNull(login, "Login User Name parameter cannot be NULL.");
		ConsumeMessages consumeMessages = null;
		// Make sure to take a Login ID for Attaching a ConsumerGroup in Future.
		String groupId = communityName + login;
		try {
			consumeMessages = new ConsumeMessages("ComplianceConsumerThread", ZK_HOSTNAME_URL, ZK_TIMEOUT,
					communityName, groupId);
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

	public ConsumeMessages(String threadName, String hostNameUrl, final String zkTimeout, String topicName,
			String groupId) throws InterruptedException, ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId);
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
