package com.lithium.streams.compliance.security;

import java.io.IOException;

import com.lithium.streams.compliance.model.SecureEvent;

import lithium.research.key.KeySource;

public interface KeyServerDecryption {
	public abstract SecureEvent performMessageDecryption(SecureEvent event, String communityName, KeySource source) throws IOException;
}
