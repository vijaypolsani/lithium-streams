package com.lithium.streams.compliance.consumer;

import kafka.javaapi.consumer.SimpleConsumer;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class SimpleConsumerPool extends GenericObjectPool<SimpleConsumer> {

	/**
	 * Constructor.
	 * Default configuration for pool provided by  apache-commons-pool2.
	 * @param factory
	 */
	public SimpleConsumerPool(PooledObjectFactory<SimpleConsumer> factory) {
		super(factory);
	}

	/**
	* Constructor.
	* Full control over the pool using configuration object.
	* @param factory
	* @param config
	*/
	public SimpleConsumerPool(PooledObjectFactory<SimpleConsumer> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

}
