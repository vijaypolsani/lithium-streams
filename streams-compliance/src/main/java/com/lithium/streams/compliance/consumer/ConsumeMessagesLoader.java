package com.lithium.streams.compliance.consumer;

import com.google.common.cache.CacheLoader;

public class ConsumeMessagesLoader extends CacheLoader<String, ConsumeMessages> {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String ZK_TIMEOUT = "5000";
	private static final String TOPIC_NAME = "vijay";

	public ConsumeMessagesLoader() {
		super();
	}

	//TODO: The NULL loader will not WORK. Need a new instance of the Service.
	@Override
	public ConsumeMessages load(String key) throws Exception {
		return new ConsumeMessages(key, ZK_HOSTNAME_URL, ZK_TIMEOUT, TOPIC_NAME, key, null);
	}
}
