package com.lithium.streams.compliance.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.lithium.streams.compliance.consumer.ConsumerGroup;

@Path("/")
public class ComplianceService {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String ZK_TIMEOUT = "5000";
	private static final Logger log = LoggerFactory.getLogger(ComplianceService.class);

	/**
	 * API for REAL TIME consumption of the events based on a given Community & User login.
	 * @param communityName
	 * @param login
	 * @return org.glassfish.jersey.media.sse.EventOutput Jersey2.x based SSE event containing 1 event for consumption at a time.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GET
	@Path("live/{communityName}")
	//@Produces(MediaType.APPLICATION_JSON)
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	@Timed
	@Metered
	@ExceptionMetered
	public EventOutput getLiveEvents(@PathParam("communityName") String communityName,
			@DefaultValue("demo") @QueryParam("login") String login) throws InterruptedException, ExecutionException {
		final EventOutput eventOutput = new EventOutput();
		if (login == null) {
			log.info(">>> Login name cannot be empty. Hence using DEMO user as default.");
		}
		if (communityName == null) {
			try {
				eventOutput.write(new OutboundEvent.Builder().data(String.class,
						"Community Name cannot be Empty or NULL").build());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return eventOutput;
		}
		//Preconditions.checkArgument(login == null, "Community Name cannot be NULL");
		// Should I have a new Thread for every customer to Kafka? Ideally I want to create a 
		// new ConsumerGroup for every Customer Login name. I can keep the thread or discard. If I generalize here,
		// then there could be events missing for this client.... TODO: More strategies...
		final String consumerGroupId = communityName + login;
		final ConsumerGroup consumerGroup = new ConsumerGroup(ZK_HOSTNAME_URL, ZK_TIMEOUT, communityName,
				consumerGroupId);
		final Thread customerStreamThread = new Thread("ComplianceServiceThread") {
			@Override
			public void run() {
				try {
					final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
					eventBuilder.name("Compliance_Service_SSE_Events");
					while (true) {
						// Hide the low level Thread Sync details after DEMO
						synchronized (consumerGroup.getLock()) {
							String str = consumerGroup.getLock().getJsonContent();
							if (str != null) {
								eventBuilder.data(String.class, str);
								final OutboundEvent event = eventBuilder.build();
								eventOutput.write(event);
							} else
								log.info(">>> Data read not started yet. Wait on stream ... " + str);
							consumerGroup.getLock().wait();
						}
					}
				} catch (IOException io) {
					log.error("<<< Looks like the current Active Thread serving Request is dead. Try create new thread & serve reqeust.");
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
				}
			}
		};
		customerStreamThread.start();
		return eventOutput;
	}

	/**
	 * API to get the Batch of Events based on the StartTimestamp and EndTimestamp
	 * @param communityName
	 * @param login
	 * @param startTime
	 * @param endTime
	 * @return String Compressed ZIP containing a batch of the events to download by the consumer.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */

	@GET
	@Path("bulk/time/{communityName}")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchTimedEvents(@PathParam("communityName") String communityName,
			@DefaultValue("demo") @QueryParam("login") String login, @QueryParam("startTime") String startTime,
			@QueryParam("endTime") String endTime) throws InterruptedException, ExecutionException {
		return "TEST";
	}

	/**
	 * API to get the Batch of Events based on the Unique Sequence StartId and EndId
	 * @param communityName
	 * @param login
	 * @param startID
	 * @param endId
	 * @return String Compressed ZIP containing a batch of the events to download by the consumer.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GET
	@Path("bulk/id/{communityName}")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchIdEvents(@PathParam("communityName") String communityName,
			@DefaultValue("demo") @QueryParam("login") String login, @QueryParam("startID") String startID,
			@QueryParam("endId") String endId) throws InterruptedException, ExecutionException {
		return "TEST";
	}

	/**
	 * API to get the last Event Unique SequenceId delivered to the Consumer. This helps in tracking the consumption of the client.
	 * @param communityName
	 * @param login
	 * @return String Last event UniqueID of the content supplied to the customer
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GET
	@Path("latest/id/{communityName}")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchLatestSequenceId(@PathParam("communityName") String communityName,
			@DefaultValue("demo") @QueryParam("login") String login) throws InterruptedException, ExecutionException {
		return "DEMO123456789";
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
	@Path("latest/time/{communityName}")
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	@Metered
	@ExceptionMetered
	public String getBatchLatestEndTime(@PathParam("communityName") String communityName,
			@DefaultValue("demo") @QueryParam("login") String login) throws InterruptedException, ExecutionException {
		return "TODAY.2.30.PST";
	}

}
