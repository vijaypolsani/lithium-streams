package com.lithium.streams.compliance.client;

import lithium.research.key.KeySource;

import com.lithium.streams.compliance.model.SecureEvent;

public interface IEncryption {
	public abstract SecureEvent performMessageEncryption(SecureEvent secureEvent, String communityName, KeySource source);
}