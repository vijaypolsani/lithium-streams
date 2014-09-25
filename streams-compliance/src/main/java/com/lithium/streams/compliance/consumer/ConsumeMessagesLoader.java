package com.lithium.streams.compliance.consumer;

import com.google.common.cache.CacheLoader;

public class ConsumeMessagesLoader extends CacheLoader<String, ConsumeMessages> {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String ZK_TIMEOUT = "5000";
	private static final String TOPIC_NAME = "lia";

	public ConsumeMessagesLoader() {
		super();
	}

	//TODO: The parameter cannot be NULL. It has to be StreamEventBus instance from bean
	@Override
	public ConsumeMessages load(String key) throws Exception {
		return new ConsumeMessages(key, ZK_HOSTNAME_URL, ZK_TIMEOUT, TOPIC_NAME, key, null);
	}
}
