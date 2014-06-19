package com.lithium.streams.compliance.exception;

public class TransformationException extends RuntimeException {
	private String message = null;

	public TransformationException() {
		// TODO Auto-generated constructor stub
	}

	public TransformationException(String message) {
		super(message);
		this.message = message;
	}

	public TransformationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TransformationException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public TransformationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}

}
