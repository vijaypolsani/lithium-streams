package com.lithium.streams.compliance.exception;

public class FilterException extends RuntimeException {
	private String message = null;

	public FilterException() {
		// TODO Auto-generated constructor stub
	}

	public FilterException(String message) {
		super(message);
		this.message = message;
	}

	public FilterException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public FilterException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public FilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.message = message;
	}

}
