package org.streams.compliance.service;

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

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

public interface ComplianceService {

	@GET
	@Path("/stream/{id}")
	//@Produces(MediaType.APPLICATION_JSON)
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	@Timed
	@Metered
	public abstract EventOutput getLiaEvents(@PathParam("id") String login) throws InterruptedException,
			ExecutionException;

	@GET
	@Path("/batch/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	public abstract EventOutput getLiaActivityStreams(@PathParam("id") String login,
			@DefaultValue("1.0") @QueryParam("version") float version) throws InterruptedException, ExecutionException;
}
