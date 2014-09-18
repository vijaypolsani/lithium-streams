package com.lithium.streams.compliance.security;

import java.io.IOException;

import com.lithium.streams.compliance.model.SecureEvent;

import lithium.research.key.KeySource;

public interface KeyServerEncryption {
	public abstract SecureEvent performMessageEncryption(SecureEvent event, String communityName, KeySource source)
			throws IOException;
}
