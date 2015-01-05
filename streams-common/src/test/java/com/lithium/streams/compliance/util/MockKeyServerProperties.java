package com.lithium.streams.compliance.util;

public enum MockKeyServerProperties {
	URI_LINK("wss://keyserver.dev.lithium.com"), EMAIL("Dev-Integration@lithium.com"), USER_KEY(
			"8A37B080BF2B15530146D9C6C7F2360A"), AES("AES"), COMMUNITY_NAME("actiance.stage"), //HOST_KEY_PATH("/home/user/host.key");
	HOST_KEY_PATH("/host.key");

	private final String value;

	MockKeyServerProperties(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
