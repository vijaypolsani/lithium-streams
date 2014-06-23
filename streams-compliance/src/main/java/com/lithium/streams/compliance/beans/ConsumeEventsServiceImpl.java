package com.lithium.streams.compliance.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Deque;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lithium.streams.compliance.consumer.ConsumeMessages;
import com.lithium.streams.compliance.consumer.ConsumerGroupRemovalListener;

public class ConsumeEventsServiceImpl implements ConsumeEventsService {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String ZK_TIMEOUT = "5000";
	private static final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);

	@Autowired
	private StreamCache streamCache;

	private ConsumeMessages consumeMessages = null;

	// Use this Service to WRAP the access to the Kafka Consumer Threads
	public ConsumeEventsServiceImpl(StreamCache streamCache) {
		this.streamCache = streamCache;
	}

	@Override
	public Deque<String> consumeEvents(String communityName, String login) throws InterruptedException,
			ExecutionException {
		checkNotNull(communityName, "Community Name parameter cannot be NULL.");
		checkNotNull(login, "Login User Name parameter cannot be NULL.");
		// Make sure to take a Login ID for Attaching a ConsumerGroup in Future.
		String groupId = communityName + login;
		log.info(">>> StreamCache Constructed, Check for NULL ? : " + streamCache);
		consumeMessages = streamCache.getCache().getIfPresent(groupId);
		if (consumeMessages == null) {
			log.info(">>> No Cache Thread *NOT FOUND* for GroupID: " + groupId);
			consumeMessages = new ConsumeMessages(groupId, ZK_HOSTNAME_URL, ZK_TIMEOUT, communityName, groupId);
			consumeMessages.start();
			streamCache.getCache().put(groupId, consumeMessages);
		}
		log.info(">>> Cache Thread *FOUND* for GroupID: " + groupId + " Stats: "
				+ streamCache.getCache().stats().toString());
		return consumeMessages.getData();
	}
}