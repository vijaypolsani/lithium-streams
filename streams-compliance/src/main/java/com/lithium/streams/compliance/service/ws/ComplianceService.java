package com.lithium.streams.compliance.service.ws;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lithium.streams.compliance.beans.ConsumeEventsService;
import com.lithium.streams.compliance.exception.ComplianceServiceException;
import com.lithium.streams.compliance.model.ComplianceEvent;
import com.lithium.streams.compliance.util.StreamEventBusListener;

@Path("/")
public class ComplianceService {
	private static final String ZK_HOSTNAME_URL = "10.240.163.94:2181";
	private static final String ZK_TIMEOUT = "5000";
	private static final Logger log = LoggerFactory.getLogger(ComplianceService.class);
	private static ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"classpath*:/spring/appContext.xml");
	//private Deque<ComplianceEvent> subscriberData = new ConcurrentLinkedDeque<ComplianceEvent>();
	private Queue<ComplianceEvent> subscriberData = new ArrayDeque<ComplianceEvent>();
	//@Autowired
	private ConsumeEventsService consumeEventsService = (ConsumeEventsService) appContext
			.getBean("consumeEventsService");

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

		checkNotNull(login, new WebApplicationException("Login Parameter Cannot be NULL", Response.Status.BAD_REQUEST));
		checkNotNull(communityName, new WebApplicationException("CommunityName Cannot be NULL",
				Response.Status.BAD_REQUEST));

		final EventOutput eventOutput = new EventOutput();
		final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		eventBuilder.name("Compliance_Service_SSE_Lia_Events");

		//TODO:
		// Preconditions.checkArgument(login == null, "Community Name cannot be NULL");
		// Should I have a new Thread for every customer to Kafka? Ideally I want to create a 
		// new ConsumerGroup for every Customer Login name. I can keep the thread or discard. If I generalize here,
		// then there could be events missing for this client.... 
		// More strategies...

		//TODO: Implement Facade pattern here. For a give Community-Check Cache to bring already streaming data
		log.info(">>> In Compliance Service. ThreadStackTrace: ID: " + Thread.currentThread().getId() + " Name: "
				+ Thread.currentThread().getName());

		// Log for Spring Beans.
		log.info(">>> consumeEventsService : " + consumeEventsService.toString());

		class StreamEventBusListenerImpl implements StreamEventBusListener {
			@Subscribe
			@AllowConcurrentEvents
			public void readEvents(ComplianceEvent complianceEvent) {
				log.info(">>> LiaPostEvent Subscribed inner class StreamEventBusListenerImpl: "
						+ complianceEvent.getEvent());
				eventBuilder.data(String.class, complianceEvent.getEvent());
				final OutboundEvent event = eventBuilder.build();
				try {
					log.info(">>> Inside ConsumerService sending to GUI.");
					eventOutput.write(event);
				} catch (IOException e) {
					e.printStackTrace();
					log.error("Exception in writing to SSE object eventOutput." + e.getLocalizedMessage());
					throw new ComplianceServiceException("LI001", "Exception in writing to SSE object eventOutput.", e);
				}
			}

		}
		consumeEventsService.consumeEvents(communityName, login, new StreamEventBusListenerImpl());
		return eventOutput;
	}

	@Subscribe
	@AllowConcurrentEvents
	public void readEvent(ComplianceEvent complianceEvent) {
		//log.info(">>> LiaPostEvent Subscribed in this ***: " + complianceEvent.getEvent());
		log.info(">>> LiaPostEvent inside Subscriber: " + complianceEvent.getEvent());
		log.info(">>> LiaPostEvent subscriberData Size: " + subscriberData.size());
		subscriberData.add(complianceEvent);
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
