package com.lithium.streams.compliance.consumer;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class KafkaSimpleConsumerFactory {

	private final GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
	private final SimpleConsumerPool simpleConsumerPool;

	public KafkaSimpleConsumerFactory() {
		genericObjectPoolConfig.setMaxIdle(10);
		genericObjectPoolConfig.setMaxTotal(10);
		genericObjectPoolConfig.setTestOnBorrow(true);
		genericObjectPoolConfig.setTestOnReturn(true);
		simpleConsumerPool = new SimpleConsumerPool(new SimpleConsumerFactory(), genericObjectPoolConfig);
	}

	public SimpleConsumerPool getPool() {
		return simpleConsumerPool;
	}

}
