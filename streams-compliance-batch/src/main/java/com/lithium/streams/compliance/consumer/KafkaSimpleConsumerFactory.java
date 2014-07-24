package com.lithium.streams.compliance.consumer;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class KafkaSimpleConsumerFactory {

	private final GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
	private final SimpleConsumerPool simpleConsumerPool;

	public KafkaSimpleConsumerFactory() {
		genericObjectPoolConfig.setMaxIdle(1);
		genericObjectPoolConfig.setMaxTotal(1);
		genericObjectPoolConfig.setTestOnBorrow(true);
		genericObjectPoolConfig.setTestOnReturn(true);
		simpleConsumerPool = new SimpleConsumerPool(new SimpleConsumerFactory(), genericObjectPoolConfig);
	}

	public SimpleConsumerPool getPool() {
		return simpleConsumerPool;
	}

}
