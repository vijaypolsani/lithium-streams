package com.lithium.streams.compliance.exception;

public class ComplianceTopologyException extends RuntimeException {
	private String message = null;

	public ComplianceTopologyException() {
		// TODO Auto-generated constructor stub
	}

	public ComplianceTopologyException(String message) {
		super(message);
		this.message = message;
	}

	public ComplianceTopologyException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ComplianceTopologyException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public ComplianceTopologyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}

}
