package com.lithium.streams.compliance.consumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import kafka.javaapi.consumer.SimpleConsumer;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleConsumerFactoryTest {

	private SimpleConsumerPool simpleConsumerPool;
	private AtomicInteger counter = new AtomicInteger(0);

	@Before
	public void setup() throws Exception {
		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setMaxIdle(1);
		genericObjectPoolConfig.setMaxTotal(1);
		genericObjectPoolConfig.setTestOnBorrow(true);
		genericObjectPoolConfig.setTestOnReturn(true);

		simpleConsumerPool = new SimpleConsumerPool(new SimpleConsumerFactory(EnumKafkaProperties.BROKER
				.getKafkaProperties(), Integer.parseInt(EnumKafkaProperties.BROKER_PORT.getKafkaProperties()), Integer
				.parseInt(EnumKafkaProperties.SO_TIMEOUT.getKafkaProperties()), Integer
				.parseInt(EnumKafkaProperties.BUFFER_SIZE.getKafkaProperties()), EnumKafkaProperties.CLIENT_NAME
				.getKafkaProperties()), genericObjectPoolConfig);
	}

	@Test
	public void simpleConsumerPoolingTest() {

		try {
			int limit = 10;
			ExecutorService es = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
					new ArrayBlockingQueue<Runnable>(limit));
			for (int i = 0; i < limit; i++) {
				Runnable r = new Runnable() {

					@Override
					public void run() {
						SimpleConsumer simpleConsumer = null;
						try {
							simpleConsumer = simpleConsumerPool.borrowObject();
							counter.getAndIncrement();
							System.out.println("Obj representation: " + simpleConsumer.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							if (simpleConsumer != null)
								simpleConsumerPool.returnObject(simpleConsumer);
						}
					}

				};
				es.submit(r);
			}
			es.shutdown();

			try {
				es.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException ignored) {
			}
			System.out.println("Pool Stats:\n Created:[" + simpleConsumerPool.getCreatedCount() + "], Borrowed:["
					+ simpleConsumerPool.getBorrowedCount() + "]");
			Assert.assertEquals(limit, counter.get());
			Assert.assertEquals(counter.get(), simpleConsumerPool.getBorrowedCount());
			Assert.assertEquals(1, simpleConsumerPool.getCreatedCount());
		} catch (Exception ex) {

		}
	}
}
