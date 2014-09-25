package com.lithium.streams.compliance.consumer;

import java.util.concurrent.ExecutionException;

import lithium.research.key.KeySource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lithium.streams.compliance.beans.ConsumeEventsServiceImpl;
import com.lithium.streams.compliance.beans.StreamEventBus;
import com.lithium.streams.compliance.client.IDecryption;
import com.lithium.streams.compliance.exception.ComplianceServiceException;
import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.model.LiaPostEvent;
import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.KeyServerProperties;
import com.lithium.streams.compliance.util.KeySourceUtil;

public class ConsumeMessages extends Thread {
	private final Logger log = LoggerFactory.getLogger(ConsumeEventsServiceImpl.class);
	private final ConsumerGroup consumerGroup;
	private final StreamEventBus streamEventBus;
	private final IDecryption iDecryption;
	private final KeySource keySource;

	public ConsumeMessages(String threadName, String hostNameUrl, final String zkTimeout, String topicName,
			String groupId, StreamEventBus streamEventBus, IDecryption iDecryption, KeySource keySource)
			throws InterruptedException, ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId, streamEventBus);
		this.streamEventBus = streamEventBus;
		this.iDecryption = iDecryption;
		this.keySource = keySource;
	}

	public ConsumeMessages(String threadName, String hostNameUrl, final String zkTimeout, String topicName,
			String groupId, StreamEventBus streamEventBus) throws InterruptedException, ExecutionException {
		super(threadName);
		consumerGroup = new ConsumerGroup(hostNameUrl, zkTimeout, topicName, groupId, streamEventBus);
		this.streamEventBus = streamEventBus;
		this.iDecryption = null;
		this.keySource = null;
	}

	public void run() {
		long counter = 0L;
		log.info(">>> Created ConsumeMessages thread for reading data from Kafka.");
		while (true) {
			//TODO: Hide the low level Thread Sync details after Performance. Remove STR and get Direct JSON Content
			synchronized (consumerGroup.getLock()) {
				log.info(">>> ConsumeMessages Handler Thread. *CC = " + counter++);
				if (consumerGroup.getLock().getJsonContent() != null) {
					try {
						//Decryption method call for enabling wrapping.
						//TODO: Aspect based wrapping for Decryption.
						if (keySource != null && iDecryption != null) {
							try {
								SecureEvent decryptedEvent = iDecryption.performMessageDecryption(new Payload(
										consumerGroup.getLock().getJsonContent()), KeyServerProperties.COMMUNITY_NAME
										.getValue(), keySource);
								consumerGroup.getLock().setJsonContent(decryptedEvent.getMessage());
							} catch (StreamsCommonSecurityException sce) {
								log.info(">>> Decryption exception occured. Moving on expected no decryption needed.");
								log.info(sce.getLocalizedMessage());
							}
						}
						//End of encryption.

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