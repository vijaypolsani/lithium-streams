package com.lithium.streams.compliance.service.ws;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

@Path("/v1")
public class ComplianceClientStateService {

	private static final Logger log = LoggerFactory.getLogger(ComplianceClientStateService.class);
	private static final String COMMUNITY_NAME = "actiance";
	//private static final String COMMUNITY_NAME = "actiance.stage";
	private static final String LOGIN = "-user";
	private static final String PASSCODE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";

	/**
	* API to get the Batch of Events based on the StartTimestamp and EndTimestamp: Client State Lookup 
	* @param communityName
	* @param login
	* @param startTime
	* @param endTime
	* @return String Compressed ZIP containing a batch of the events to download by the consumer.
	* @throws InterruptedException
	* @throws ExecutionException
	*/
	@GET
	@Path("client/time")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchTimedEvents(@DefaultValue("demo") @QueryParam("login") String login)
			throws InterruptedException, ExecutionException {
		return "{'Jan12,2012,12.44PMPST','Jan13,2012,12.44PMPST'}";
	}

	/**
	 * API to get the Batch of Events based on the Unique Sequence StartId and EndId : Client State Lookup 
	 * @param communityName
	 * @param login
	 * @param startID
	 * @param endId
	 * @return String Compressed ZIP containing a batch of the events to download by the consumer.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GET
	@Path("client/id")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchIdEvents(@DefaultValue("demo") @QueryParam("login") String login)
			throws InterruptedException, ExecutionException {
		return "{'123456789','223456789'}";
	}

}
