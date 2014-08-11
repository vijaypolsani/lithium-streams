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
			log.debug(">>> Is Message Valid? " + isExisting);
			//log.debug("Worker Name: " + this.hashCode() + " Result:" + result);
			if (isExisting) {
				log.debug(">>> Success. Message passes Filter Criteria: "
						+ work.getUnfilteredMessage().getEventPayload().getJsonMessage());
			} else
				log.debug("<<< Failed. Message DO NOT PASS Filter Criteria: "
						+ work.getUnfilteredMessage().getEventPayload().getJsonMessage());
			getSender().tell(new WorkResponse(work, isExisting), getSelf());

		} else {
			unhandled(message);
		}

	}
}
