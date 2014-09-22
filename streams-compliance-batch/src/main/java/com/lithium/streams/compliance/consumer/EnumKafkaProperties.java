package com.lithium.streams.compliance.consumer;

public enum EnumKafkaProperties {
	//PROD
	ZK("10.220.186.232:2181"),
	BROKER("10.220.186.232"),
	//DEV
	//ZK("10.240.163.94:2181"), 
	//BROKER("10.240.163.94"),
	GROUP_ID("batch"), TOPIC("lia"), TOPIC_ACTIANCE("actiance.stage"), BROKER_PORT("9092"), BUFFER_SIZE("65536"), CONNECTION_TIMEOUT(
			"10000"), RECONNECTION_TIMEOUT("10000"), CLIENT_ID("batch"), PARTITION("0"), MESSAGE_SIZE("1024000"), CLIENT_NAME(
			"ComplianceBatchConsumer");

	private String kafkaProperties;

	private EnumKafkaProperties(String value) {
		this.kafkaProperties = value;
	}

	public String getKafkaProperties() {
		return kafkaProperties;
	}
}
