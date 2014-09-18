package com.lithium.streams.compliance.exception;

public class HostKeyMachineMisMatchSecurityException extends RuntimeException {
	private static final long serialVersionUID = -7596984655706421390L;
	private String message = null;
	private String errorCode = null;

	public HostKeyMachineMisMatchSecurityException() {
		// TODO Auto-generated constructor stub
	}

	public HostKeyMachineMisMatchSecurityException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public HostKeyMachineMisMatchSecurityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public HostKeyMachineMisMatchSecurityException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.errorCode = errorCode;
	}

	public HostKeyMachineMisMatchSecurityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
