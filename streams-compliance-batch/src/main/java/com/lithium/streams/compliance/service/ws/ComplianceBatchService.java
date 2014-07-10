package com.lithium.streams.compliance.service.ws;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Calendar;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

@Path("/v1")
public class ComplianceBatchService {

	private static final Logger log = LoggerFactory.getLogger(ComplianceBatchService.class);
	private static final String COMMUNITY_NAME = "actiance";
	//private static final String COMMUNITY_NAME = "actiance.stage";
	private static final String LOGIN = "-user";
	private static final String PASSCODE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";

	/**
	 * API to get the last Event Unique SequenceId SET delivered to the Consumer. The startID & endID are required
	 * @param communityName
	 * @param login
	 * @return String Last event UniqueID of the content supplied to the customer
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GET
	@Path("id")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public Response getBatchLatestSequenceId(@HeaderParam("client-id") String clientId,
			@QueryParam("startId") BigInteger startId, @QueryParam("endId") BigInteger endId)
			throws InterruptedException, ExecutionException {

		StreamingOutput streamingOutput = new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

			}
		};
		return Response.ok().entity(streamingOutput).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * API to get the last Event TimeStamp delivered to the Consumer. This helps in tracking the consumption of the client.
	 * @param communityName
	 * @param login
	 * @return String Last event timestamp of the content supplied to the customer
	 * @throws InterruptedException
	 * @throws ExecutionException
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
