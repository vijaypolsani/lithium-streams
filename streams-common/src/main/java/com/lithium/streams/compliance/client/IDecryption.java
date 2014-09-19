package com.lithium.streams.compliance.client;

import lithium.research.key.KeySource;

import com.lithium.streams.compliance.model.SecureEvent;

public interface IDecryption {
	public abstract SecureEvent performMessageDecryption(SecureEvent secureEvent, String communityName, KeySource source);
}