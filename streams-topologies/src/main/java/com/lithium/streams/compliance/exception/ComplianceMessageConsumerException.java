package com.lithium.streams.compliance.exception;

public class ComplianceMessageConsumerException extends RuntimeException {
	private String message = null;

	public ComplianceMessageConsumerException() {
		// TODO Auto-generated constructor stub
	}

	public ComplianceMessageConsumerException(String message) {
		super(message);
		this.message = message;
	}

	public ComplianceMessageConsumerException(Throwable cause) {
		super(cause);
	}

	public ComplianceMessageConsumerException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public ComplianceMessageConsumerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}

}
