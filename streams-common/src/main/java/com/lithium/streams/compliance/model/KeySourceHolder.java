package com.lithium.streams.compliance.model;

import java.time.Instant;
import java.util.Optional;

import lithium.research.key.KeySource;

public class KeySourceHolder {

	private final long timeEpochMilli = System.nanoTime();
	private final Optional<KeySource> keysource;

	public KeySourceHolder(Optional<KeySource> keySource) {
		this.keysource = keySource;
	}

	/**
	 * @return the timeEpochMilli
	 */
	public long getTimeEpochMilli() {
		return timeEpochMilli;
	}

	/**
	 * @return the keysource
	 */
	public Optional<KeySource> getKeysource() {
		return keysource;
	}
}
