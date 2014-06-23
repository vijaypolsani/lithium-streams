package com.lithium.streams.compliance.exception;

public class ComplianceServiceException extends RuntimeException {
	private String message = null;
	private String errorCode = null;

	public ComplianceServiceException() {
		// TODO Auto-generated constructor stub
	}

	public ComplianceServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ComplianceServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ComplianceServiceException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.errorCode = errorCode;
	}

	public ComplianceServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
