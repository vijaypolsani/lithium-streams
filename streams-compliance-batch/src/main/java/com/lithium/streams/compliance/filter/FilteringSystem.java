package com.lithium.streams.compliance.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.actor.dsl.Inbox.Inbox;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.lithium.streams.compliance.api.MessageFilteringService;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.model.CompliancePayload;
import com.lithium.streams.compliance.util.BlockingQueueOfOne;

public class FilteringSystem {
	private static final int NUM_WORKERS = 6;
	private static final String ACTOR_SYSTEM_NAME = "ComplianceFilterSystem";
	private static final String ACTOR_SYSTEM_LISTENER = "ComplianceFilterListener";
	private static final String ACTOR_SYSTEM_MASTER = "ComplianceFilterMaster";
	private final ActorRef master;
	private final ActorRef listener;
	private final ActorSystem system;
	private final akka.actor.Inbox inbox;
	public static final Logger log = LoggerFactory.getLogger(FilteringSystem.class);

	public FilteringSystem() {
		// Create an Akka system
		this.system = ActorSystem.create(ACTOR_SYSTEM_NAME);
		//Create Inbox
		this.inbox = Inbox.create(system);
		// create the result listener, which will print the result and shutdown the system
		this.listener = system.actorOf(Props.create(FilterResponseListener.class), ACTOR_SYSTEM_LISTENER);
		// create the master
		this.master = system.actorOf(Props.create(new MasterFactory(NUM_WORKERS, listener)), ACTOR_SYSTEM_MASTER);

	}

	public ActorRef getMaster() {
		return master;
	}

	public ActorSystem getActorSystem() {
		return system;
	}

	public akka.actor.Inbox getInbox() {
		return inbox;
	}

	public ActorRef getListener() {
		return listener;
	}

	public void filter(Collection<ComplianceMessage> messages, long startTime, long endTime) {
		// start the process of Filtering
		//INFO: A large volume of the messages needs workers to run in parallel to complete. A single message
		//filter is not efficient as time taken to do a single message filter do not need scaling. We are not taking
		//about large JSON files or Terabytes of data here. Small message filtering. Hence bunch acceptance.
		master.tell(new Request(messages, startTime, endTime), ActorRef.noSender());
	}

	public static void main(String[] args) {
		//1 year
		long startTime = System.currentTimeMillis() - 9999999999l;
		long endTime = System.currentTimeMillis();
		System.out.println("Start Time : " + startTime);
		System.out.println("End Time : " + endTime);

		Collection<ComplianceMessage> messages = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			ComplianceMessage msg = ComplianceMessage.MsgBuilder.init("junit" + i).payload(
					CompliancePayload.init(MessageFilteringService.SAMPLE_INPUT.getBytes())).build();
			messages.add(msg);
		}
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));

		FilteringSystem filteringSystem = new FilteringSystem();
		Future<Object> future = Patterns.ask(filteringSystem.getMaster(), new Request(messages, startTime, endTime),
				timeout);
		try {
			String result = (String) Await.result(future, timeout.duration());
			System.out.println("Result: " + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//filteringSystem.filter(messages, startTime, endTime);
		//String inputJsonStream, long startTime, long endTime

		filteringSystem.inbox.send(filteringSystem.getMaster(), new Request(messages, startTime, endTime));
		//filteringSystem.inbox.watch(filteringSystem.getSubscriber());
		//filteringSystem.inbox.watch(filteringSystem.getListener());
		//System.out.println(">>>Received message from FilterSystem: "+ filteringSystem.inbox.receive(Duration.create(5, TimeUnit.SECONDS)).toString());
		//System.out.println(" Inbox ActorRef: " + filteringSystem.getListener().toString());
	}
}
