package com.lithium.streams.compliance.consumer;

import kafka.javaapi.consumer.SimpleConsumer;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SimpleConsumerFactory extends BasePooledObjectFactory<SimpleConsumer> {

	@Override
	public SimpleConsumer create() throws Exception {
		return new SimpleConsumer(EnumKafkaProperties.BROKER.getKafkaProperties(), Integer
				.parseInt(EnumKafkaProperties.BROKER_PORT.getKafkaProperties()), Integer
				.parseInt(EnumKafkaProperties.MESSAGE_SIZE.getKafkaProperties()), Integer
				.parseInt(EnumKafkaProperties.BUFFER_SIZE.getKafkaProperties()), EnumKafkaProperties.CLIENT_NAME
				.getKafkaProperties());
	}

	@Override
	public PooledObject<SimpleConsumer> wrap(SimpleConsumer obj) {
		return new DefaultPooledObject<SimpleConsumer>(obj);
	}

}
