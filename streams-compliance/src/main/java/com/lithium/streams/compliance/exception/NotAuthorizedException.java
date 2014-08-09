package com.lithium.streams.compliance.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotAuthorizedException extends WebApplicationException {
	public NotAuthorizedException(String message) {
		super(Response.status(Response.Status.FORBIDDEN).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
