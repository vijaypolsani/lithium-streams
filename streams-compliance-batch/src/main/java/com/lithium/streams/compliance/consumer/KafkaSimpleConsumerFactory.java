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
		simpleConsumerPool = new SimpleConsumerPool(new SimpleConsumerFactory(EnumKafkaProperties.CLIENT_NAME
				.getKafkaProperties(), EnumKafkaProperties.BROKER.getKafkaProperties(), Integer
				.parseInt(EnumKafkaProperties.MESSAGE_SIZE.getKafkaProperties()), Integer
				.parseInt(EnumKafkaProperties.BUFFER_SIZE.getKafkaProperties()), Integer
				.parseInt(EnumKafkaProperties.BROKER_PORT.getKafkaProperties())), genericObjectPoolConfig);
	}

	public SimpleConsumerPool getPool() {
		return simpleConsumerPool;
	}

}
