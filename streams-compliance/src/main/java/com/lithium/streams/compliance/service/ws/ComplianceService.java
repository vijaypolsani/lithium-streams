package com.lithium.streams.compliance.service.ws;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
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

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lithium.streams.compliance.beans.ConsumeEventsService;
import com.lithium.streams.compliance.beans.StreamEventBus;
import com.lithium.streams.compliance.exception.ComplianceServiceException;
import com.lithium.streams.compliance.exception.NotAuthorizedException;
import com.lithium.streams.compliance.model.ComplianceEvent;
import com.lithium.streams.compliance.util.StreamEventBusListener;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

@Path("/v1")
public class ComplianceService {

	private static final Logger log = LoggerFactory.getLogger(ComplianceService.class);
	//private static final String COMMUNITY_NAME = "actiance";
	private static final String COMMUNITY_NAME = "actiance.stage";
	private static final String LOGIN = "-user";
	private static final String PASSCODE = "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=";

	@Inject
	private ConsumeEventsService consumeEventsService;
	@Inject
	private StreamEventBus streamEventBus;

	/**
	 * API for REAL TIME consumption of the events based on a given Community & User login.
	 * @param communityName
	 * @param login
	 * @return org.glassfish.jersey.media.sse.EventOutput Jersey2.x based SSE event containing 1 event for consumption at a time.
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GET
	@Path("live")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	@Timed
	@Metered
	@ExceptionMetered
	public EventOutput getLiveEvents(@HeaderParam("client-id") String clientId) throws InterruptedException,
			ExecutionException {

		//TODO: Ideally I want to create a new ConsumerGroup for every Customer Login name. I can keep the thread or discard. 

		log.info(">>> In Compliance Service. Header client-id: " + clientId);
		if (clientId == null || !(clientId.equals(PASSCODE))) {
			log.info("Missing Header 'client-id' OR Invalid Passcode for header client-id: " + clientId);
			throw new NotAuthorizedException(
					"Unauthorized Request: Missing Header 'client-id' OR Invalid Passcode for header.");
		}

		final EventOutput eventOutput = new EventOutput();
		final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		eventBuilder.name("Compliance_Service_SSE_Lia_Events");
		final class StreamEventBusListenerImpl implements StreamEventBusListener {
			final String listenerName;

			StreamEventBusListenerImpl(String listenerClientName) {
				this.listenerName = listenerClientName;
			}

			@SuppressWarnings("unused")
			@Subscribe
			@AllowConcurrentEvents
			public void readEvents(ComplianceEvent complianceEvent) {
				//log.info(">>> LiaPostEvent Subscribed inner class StreamEventBusListenerImpl: "+ complianceEvent.getEvent());
				eventBuilder.data(String.class, complianceEvent.getEvent());
				final OutboundEvent event = eventBuilder.build();
				try {
					if (eventOutput != null) {
						log.info(">>> ConsumerService sending Events to Client: " + listenerName);
						eventOutput.write(event);
					} else
						log.error("--- ConsumerService sending Events closed Null: " + listenerName);

				} catch (IOException e) {
					e.printStackTrace();
					//streamEventBus.unRegisterSubscriber(this);
					log.error("Exception in writing to SSE object eventOutput. Removed Client Listener. "
							+ listenerName + " Exception: " + e.getLocalizedMessage());
					//throw new ComplianceServiceException("LI001", "Exception in writing to SSE object eventOutput.", e);
				}
			}
		}
		streamEventBus.registerSubscriber(new StreamEventBusListenerImpl("ThreadID: " + Thread.currentThread().getId()
				+ " ThreadName: " + Thread.currentThread().getName()));
		consumeEventsService.consumeEvents(COMMUNITY_NAME, LOGIN);
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
		return Calendar.getInstance().getTime().toString();
	}

}
