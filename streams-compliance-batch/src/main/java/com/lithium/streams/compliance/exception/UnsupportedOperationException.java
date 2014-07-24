package com.lithium.streams.compliance.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UnsupportedOperationException extends WebApplicationException {
	private static final long serialVersionUID = -7214268909582764181L;
	private String message = null;
	private String errorCode = null;

	public UnsupportedOperationException(String message) {
		super(Response.status(Response.Status.NOT_IMPLEMENTED).entity(message).type(MediaType.TEXT_PLAIN).build());
	}

	public UnsupportedOperationException() {
	}

	public UnsupportedOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedOperationException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.errorCode = errorCode;
	}
}
