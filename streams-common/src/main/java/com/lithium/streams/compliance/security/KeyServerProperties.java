package com.lithium.streams.compliance.security;

public enum KeyServerProperties {
	//TODO: This configuration needs to be externalized. Issues with packaging, so keeping it hardcoded.
	URI_LINK("wss://keyserver.dev.lithium.com"), EMAIL("Dev-Integration@lithium.com"), USER_KEY(
			"8A37B080BF2B15530146D9C6C7F2360A"), AES("AES"), COMMUNITY_NAME("actiance.stage"), HOST_KEY_PATH(
			"/home/user/host.key");
	//HOST_KEY_PATH("./conf/host.key")
	//HOST_KEY_PATH("/Users/vijay.polsani/host.key");

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
