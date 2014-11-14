package com.lithium.streams.compliance.consumer;

import kafka.javaapi.consumer.SimpleConsumer;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SimpleConsumerFactory extends BasePooledObjectFactory<SimpleConsumer> {

	private final String clientName;
	private final String brokerName;
	private final int port;
	private final int messageSize;
	private final int bufferSize;

	public SimpleConsumerFactory(String clientName, String brokerName, int port, int messageSize, int bufferSize) {
		this.clientName = clientName;
		this.brokerName = brokerName;
		this.port = port;
		this.messageSize = messageSize;
		this.bufferSize = bufferSize;
	}

	@Override
	public SimpleConsumer create() throws Exception {
		return new SimpleConsumer(brokerName, port, messageSize, bufferSize, clientName);
	}

	@Override
	public PooledObject<SimpleConsumer> wrap(SimpleConsumer obj) {
		return new DefaultPooledObject<SimpleConsumer>(obj);
	}

}
