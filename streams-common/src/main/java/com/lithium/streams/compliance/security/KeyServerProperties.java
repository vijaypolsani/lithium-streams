package com.lithium.streams.compliance.security;

public enum KeyServerProperties {
	URI_LINK("wss://keyserver.dev.lithium.com"), EMAIL("megha.meshram@lithium.com"), USER_KEY(
			"3EB5C99BF82BFBB5909B8A7CE5053643"), AES("AES"), COMMUNITY_NAME(
			"actiance.stage"), //HOST_KEY_PATH("/home/user/host.key");
			HOST_KEY_PATH("/conf/host.key");
	
	
	private final String value;

	KeyServerProperties(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
}
