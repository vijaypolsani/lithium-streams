package com.lithium.streams.compliance.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lithium.streams.compliance.consumer.EnumKafkaProperties;

public class KafkaPropertiesTest {

	private EnumKafkaProperties kafkaProperties;

	public KafkaPropertiesTest() {
	}

	@Before
	public void setup() {
		kafkaProperties = kafkaProperties.ZK;
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testValuesOfKafkaProperties() {
		switch (kafkaProperties) {
		case ZK:
			System.out.println("ZK Value: " + kafkaProperties.getKafkaProperties());
			Assert.assertSame(kafkaProperties.getKafkaProperties(), "10.240.163.94:2181");
			break;
		default:
			break;
		}
	}

}
