package com.lithium.streams.compliance.consumer;

public enum EnumKafkaProperties {
	ZK("10.240.163.94:2181"), GROUP_ID("junit"), TOPIC("lia"), TOPIC_ACTIANCE("actiance.stage"), BROKER("10.240.163.94"), BROKER_PORT(
			"9092"), BUFFER_SIZE("65536"), CONNECTION_TIMEOUT("100000"), RECONNECTION_TIMEOUT("10000"), CLIENT_ID(
			"junit"), PARTITION("0"), MESSAGE_SIZE("1024000"), CLIENT_NAME("ComplianceBatchConsumer");

	private String kafkaProperties;

	private EnumKafkaProperties(String value) {
		this.kafkaProperties = value;
	}

	public String getKafkaProperties() {
		return kafkaProperties;
	}
}
