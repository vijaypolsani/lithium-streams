package com.lithium.streams.compliance.exception;

public class ComplianceMessageSendException extends RuntimeException {
	private String message = null;

	public ComplianceMessageSendException() {
		// TODO Auto-generated constructor stub
	}

	public ComplianceMessageSendException(String message) {
		super(message);
		this.message = message;
	}

	public ComplianceMessageSendException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ComplianceMessageSendException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public ComplianceMessageSendException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}

}
