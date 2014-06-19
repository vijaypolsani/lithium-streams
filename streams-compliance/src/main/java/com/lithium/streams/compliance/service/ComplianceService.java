package com.lithium.streams.compliance.service;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

@Path("v1")
public class ComplianceService {

	@GET
	@Path("/live/{id}")
	//@Produces(MediaType.APPLICATION_JSON)
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	@Timed
	@Metered
	@ExceptionMetered
	public EventOutput getLiveEvents(@PathParam("id") String login) throws InterruptedException, ExecutionException {
		return null;
	}

	@GET
	@Path("/bulk/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchEvents(@PathParam("id") String login,
			@DefaultValue("1.0") @QueryParam("version") float version) throws InterruptedException, ExecutionException {
		return "TEST";
	}
}
