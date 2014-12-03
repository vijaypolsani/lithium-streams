package com.lithium.streams.compliance.consumer;

public enum EnumKafkaProperties {
	//I should have made this configurable. But time constraint and deadline for demo.
	//PROD
	ZK("10.220.186.232:2181"), BROKER("10.220.186.232"),
	//DEV
	//ZK("10.240.163.94:2181"), 
	//BROKER("10.240.163.94"),
	TOPIC("lia"), TOPIC_ACTIANCE("actiance.stage"), BROKER_PORT("9092"), BUFFER_SIZE("65536"), SO_TIMEOUT("100000"), PARTITION(
			"0"), MESSAGE_SIZE("100000"), CLIENT_NAME("ComplianceBatchConsumer");

	private String kafkaProperties;

	private EnumKafkaProperties(String value) {
		this.kafkaProperties = value;
	}

	public String getKafkaProperties() {
		return kafkaProperties;
	}
}
