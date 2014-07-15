package com.lithium.streams.compliance.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lithium.streams.compliance.consumer.ConsumeMessages;
import com.lithium.streams.compliance.util.StreamEventBusListener;

public class ConsumeEventsServiceImpl implements ConsumeEventsService {
	//private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	//private static final String ZK_TIMEOUT = "5000";
	private final String ZK_HOSTNAME_URL;
	private final String ZK_TIMEOUT;
	private static final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);

	@Inject
	private StreamCache streamCache;

	@Inject
	private StreamEventBus streamEventBus;

	private ConsumeMessages consumeMessages = null;

	// Use this Service to WRAP the access to the Kafka Consumer Threads
	public ConsumeEventsServiceImpl(String hostUrl, String zkTimeout) {
		this.ZK_HOSTNAME_URL = hostUrl;
		this.ZK_TIMEOUT = zkTimeout;
	}

	@Override
	public void consumeEvents(String communityName, String login) throws InterruptedException, ExecutionException {
		checkNotNull(communityName, "Community Name parameter cannot be NULL.");
		checkNotNull(login, "Login User Name parameter cannot be NULL.");
		// Make sure to take a Login ID for Attaching a ConsumerGroup in Future.
		String groupId = communityName + login;
		log.info(">>> StreamCache Constructed, Check for NULL ? : " + streamCache.hashCode());
		consumeMessages = streamCache.getCache().getIfPresent(groupId);
		if (consumeMessages == null) {
			log.info(">>> No Cache Thread *NOT FOUND* for GroupID: " + groupId);
			consumeMessages = new ConsumeMessages(groupId, ZK_HOSTNAME_URL, ZK_TIMEOUT, communityName, groupId,
					streamEventBus);
			consumeMessages.start();
			streamCache.getCache().put(groupId, consumeMessages);
		}
		log.info(">>> Cache Thread *FOUND* for GroupID: " + groupId + " HadhCode: " + streamCache.hashCode()
				+ "Stats: " + streamCache.getCache().stats().toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consumeMessages == null) ? 0 : consumeMessages.hashCode());
		result = prime * result + ((streamCache == null) ? 0 : streamCache.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsumeEventsServiceImpl other = (ConsumeEventsServiceImpl) obj;
		if (consumeMessages == null) {
			if (other.consumeMessages != null)
				return false;
		} else if (!consumeMessages.equals(other.consumeMessages))
			return false;
		if (streamCache == null) {
			if (other.streamCache != null)
				return false;
		} else if (!streamCache.equals(other.streamCache))
			return false;
		return true;
	}
}