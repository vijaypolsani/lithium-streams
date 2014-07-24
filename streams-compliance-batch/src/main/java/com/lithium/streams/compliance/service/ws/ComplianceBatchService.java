package com.lithium.streams.compliance.service.ws;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.lithium.streams.compliance.api.ComplianceBatchEvents;
import com.lithium.streams.compliance.model.ComplianceMessage;

@Path("/v1")
public class ComplianceBatchService  {

	private static final Logger log = LoggerFactory.getLogger(ComplianceBatchService.class);
	private static final String COMMUNITY_NAME = "actiance";
	//private static final String COMMUNITY_NAME = "actiance.stage";
	private static final String LOGIN = "-user";
	private static final String PASSCODE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";

	/* (non-Javadoc)
	 * @see com.lithium.streams.compliance.service.ws.ComplianceBatch#getBatchLatestSequenceId(java.lang.String, java.math.BigInteger, java.math.BigInteger)
	 */
	@GET
	@Path("id")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public Response getBatchLatestSequenceId(@HeaderParam("client-id") String clientId,
			@QueryParam("startId") String startId, @QueryParam("endId") String endId) throws InterruptedException,
			ExecutionException {

		final StreamingOutput streamingOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

			}
		};
		return Response.ok().entity("").type(MediaType.APPLICATION_JSON).build();
	}

	/* (non-Javadoc)
	 * @see com.lithium.streams.compliance.service.ws.ComplianceBatch#getBatchLatestEndTime(java.lang.String, java.lang.String, java.lang.String)
	 */

	@GET
	@Path("time")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public Response getBatchLatestEndTime(@HeaderParam("client-id") String clientId,
			@QueryParam("startId") String startTime, @QueryParam("endId") String endTime) throws InterruptedException,
			ExecutionException {
		StreamingOutput streamingOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

			}
		};
		return Response.ok().entity(streamingOutput).type(MediaType.APPLICATION_JSON).build();
	}

}
