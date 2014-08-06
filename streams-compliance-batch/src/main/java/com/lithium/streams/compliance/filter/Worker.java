package com.lithium.streams.compliance.filter;

import akka.actor.UntypedActor;

import com.lithium.streams.compliance.api.MessageFilteringService;

public class Worker extends UntypedActor {

	public Worker() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Work) {
			Work work = (Work) message;
			boolean isExisting = MessageFilteringService.filterByTime(MessageFilteringService.SAMPLE_INPUT, work
					.getStartTime(), work.getEndTime());
			System.out.println("Message Valid: " + isExisting);
			//System.out.println("Worker Name: " + this.hashCode() + " Result:" + result);
			if (isExisting) {
				getSender().tell(work, getSelf());
				System.out.println("Success. The message passes the Filter Criteria: ");
			} else
				System.out.println("Failed. The message is not in the Filter Criteria: "
						+ work.getUnfilteredMessage().getEventPayload().getJsonMessage());

		} else {
			unhandled(message);
		}

	}
}
