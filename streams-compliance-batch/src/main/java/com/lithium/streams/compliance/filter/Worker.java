package com.lithium.streams.compliance.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;

import com.lithium.streams.compliance.api.MessageFilteringService;

public class Worker extends UntypedActor {
	private static final Logger log = LoggerFactory.getLogger(Worker.class);

	public Worker() {
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Work) {
			Work work = (Work) message;
			boolean isExisting = MessageFilteringService.filterByTime(work.getUnfilteredMessage().getEventPayload()
					.getJsonMessage(), work.getStartTime(), work.getEndTime());
			log.debug("Message Valid? " + isExisting);
			//System.out.println("Worker Name: " + this.hashCode() + " Result:" + result);
			if (isExisting) {
				getSender().tell(work, getSelf());
				log.debug("Success. Message passes Filter Criteria: ");
			} else
				log.debug("Failed. Message DO NOT PASS Filter Criteria: "
						+ work.getUnfilteredMessage().getEventPayload().getJsonMessage());

		} else {
			unhandled(message);
		}

	}
}
