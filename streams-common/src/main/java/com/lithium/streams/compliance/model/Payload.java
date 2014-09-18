package com.lithium.streams.compliance.model;


public class Payload implements SecureEvent {
	// KeryServer does not work with String as the storage. Use Only ByteArray for storage.
	private final byte[] message;

	public Payload(byte[] message) {
		this.message = message;
	}

	public byte[] getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Event [message=" + new String(message) + "]";
	}
}
