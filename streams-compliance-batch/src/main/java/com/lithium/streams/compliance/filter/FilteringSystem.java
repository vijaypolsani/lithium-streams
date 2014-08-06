package com.lithium.streams.compliance.filter;

import java.util.ArrayList;
import java.util.Collection;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.lithium.streams.compliance.api.MessageFilteringService;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.model.CompliancePayload;

public class FilteringSystem {
	private static final int NUM_WORKERS = 8;
	private static final String ACTOR_SYSTEM_NAME = "ComplianceFilterSystem";
	private static final String ACTOR_SYSTEM_LISTENER = "ComplianceFilterListener";
	private static final String ACTOR_SYSTEM_MASTER = "ComplianceFilterMaster";
	private final ActorRef master;

	public FilteringSystem() {
		this.master = getMasterActor();
	}

	private ActorRef getMasterActor() {
		// Create an Akka system
		ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
		// create the result listener, which will print the result and shutdown the system
		final ActorRef listener = system.actorOf(Props.create(FilterResponseListener.class), ACTOR_SYSTEM_LISTENER);
		// create the master
		final ActorRef master = system.actorOf(Props.create(new MasterFactory(NUM_WORKERS, listener)),
				ACTOR_SYSTEM_MASTER);
		return master;
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
					CompliancePayload.init(MessageFilteringService.SAMPLE_INPUT)).build();
			messages.add(msg);
		}
		FilteringSystem filteringSystem = new FilteringSystem();
		filteringSystem.filter(messages, startTime, endTime);
		//String inputJsonStream, long startTime, long endTime
	}
}
