package com.lithium.streams.compliance.service.ws;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
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
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.service.ComplianceBatchStandalone;
import com.lithium.streams.compliance.util.Compress;
import com.lithium.streams.compliance.util.JsonFormatToArray;

@Path("/v1")
public class ComplianceBatchService {

	private static final Logger log = LoggerFactory.getLogger(ComplianceBatchService.class);
	//private static final String COMMUNITY_NAME = "lia";
	private static final String COMMUNITY_NAME = "actiance.stage";
	private static final String LOGIN = "-user";
	private static final String PASSCODE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";
	private static final String IS_NUMBER_MATCHER = ".*\\d.*";

	@Inject
	private ComplianceBatchStandalone complianceBatchStandalone;

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Compress
	@Timed
	@Metered
	@ExceptionMetered
	public Response getMessages(@HeaderParam("client-id") String clientId) throws InterruptedException,
			ExecutionException {
		log.info(">> BatchLatestSequenceId Jersey Interface Called. " + clientId);
		try {
			//Without Filtering.
			//final Collection<ComplianceMessage> messages = complianceBatchStandalone.processStream(COMMUNITY_NAME);
			//With Filtering.

			final Collection<ComplianceMessage> messages = complianceBatchStandalone.getMessagesFilteredByTime(
					COMMUNITY_NAME, "", "");

			System.out.println(">> Data size Retrieved from kafka. " + messages.size());
			final StreamingOutput streamingOutput = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					String formattedData = JsonFormatToArray.convertToJsonArray(messages);
					output.write(formattedData.getBytes());
					output.flush();
				}
			};
			return Response.ok().entity(streamingOutput).build();
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.serverError().entity(e1.getLocalizedMessage()).build();
		}
	}

	/* (non-Javadoc)
	 * @see com.lithium.streams.compliance.service.ws.ComplianceBatch#getBatchLatestSequenceId(java.lang.String, java.math.BigInteger, java.math.BigInteger)
	 */
	@GET
	@Path("id")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Compress
	@Timed
	@Metered
	@ExceptionMetered
	public Response getMessagesFilterdByTime(@HeaderParam("client-id") String clientId,
			@QueryParam("start") String start, @QueryParam("end") String end) throws InterruptedException,
			ExecutionException {
		log.info(">>> Batch Messages from REST Call. clientId: " + clientId + " start=" + start + " end=" + end);
		try {
			//IMP: START: REMOVE: Testing. REMOVE After DEMO.
			// JavaScript tend to send '{}' and has to be verified for NON A NUMBER, to stop causing NumberFormatException.
			if (start == null || start.trim().length() == 0 || !start.matches(IS_NUMBER_MATCHER))
				start = (System.currentTimeMillis() - 9999999999l) + "";
			if (end == null || end.trim().length() == 0 || !end.matches(IS_NUMBER_MATCHER))
				end = System.currentTimeMillis() + "";
			//REMOVE: END 

			//With Filtering.
			final Collection<ComplianceMessage> messages = complianceBatchStandalone.getMessagesFilteredByTime(
					COMMUNITY_NAME, start, end);
			log.info(">> Data size Retrieved from kafka. " + messages.size());
			final StreamingOutput streamingOutput = new StreamingOutput() {
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					String formattedData = JsonFormatToArray.convertToJsonArray(messages);
					output.write(formattedData.getBytes());
					output.flush();
				}
			};
			return Response.ok().entity(streamingOutput).build();
			//TODO: Is it possible to send single JSON messages one by one. The UI Logic or Client has to change.
			//With an array, client can process differently than with a single messages. Reason to think....
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.serverError().entity(e1.getLocalizedMessage()).build();
		}
	}

	/* (non-Javadoc)
	 * @see com.lithium.streams.compliance.service.ws.ComplianceBatch#getBatchLatestEndTime(java.lang.String, java.lang.String, java.lang.String)
	 */

	@GET
	@Path("time")
	//@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Compress
	@Timed
	@Metered
	@ExceptionMetered
	public Response getMessagesFilterdById(@HeaderParam("client-id") String clientId,
			@QueryParam("startId") String startTime, @QueryParam("endId") String endTime) throws InterruptedException,
			ExecutionException {
		final StreamingOutput streamingOutput = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {

			}
		};
		return Response.ok().entity(streamingOutput).build();
	}

}
