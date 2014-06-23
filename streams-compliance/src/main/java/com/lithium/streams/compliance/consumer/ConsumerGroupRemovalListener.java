package com.lithium.streams.compliance.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class ConsumerGroupRemovalListener implements RemovalListener<String, ConsumeMessages> {
	private static final Logger log = LoggerFactory.getLogger(ConsumerGroupRemovalListener.class);

	public ConsumerGroupRemovalListener() {

	}

	@Override
	public void onRemoval(RemovalNotification<String, ConsumeMessages> removalNotification) {
		log.info("Person associated with the key(" + removalNotification.getKey() + ") is removed.");
	}

}
