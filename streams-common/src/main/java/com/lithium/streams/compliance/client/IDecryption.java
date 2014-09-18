package com.lithium.streams.compliance.client;

import com.lithium.streams.compliance.model.SecureEvent;

public interface IDecryption {
	public abstract byte[] performMessageDecryption(SecureEvent secureEvent);
}