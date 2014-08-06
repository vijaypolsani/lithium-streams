package com.lithium.streams.compliance.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lithium.streams.compliance.model.ComplianceMessage;

import scala.collection.Traversable;
import scala.collection.immutable.Iterable;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public class Master extends UntypedActor {
	private static final long start = System.currentTimeMillis();
	private final int nrOfWorkers;

	private int nrOfResults;
	private Collection<ComplianceMessage> filteredMessages = new ArrayList<>();

	private final ActorRef listener;
	private final ActorRef workerRouter;

	public Master(int nrOfWorkers, ActorRef listener) {
		this.listener = listener;
		this.nrOfWorkers = nrOfWorkers;
		this.workerRouter = this.getContext().actorOf(
				Props.create(Worker.class).withRouter(new RoundRobinRouter(nrOfWorkers)));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		long numberOfMessages = 0L;
		if (message instanceof Request) {
			Request request = (Request) message;
			int start = 0;
			numberOfMessages = request.getUnfilteredMessages().size();
			for (ComplianceMessage msg : request.getUnfilteredMessages()) {
				System.out.println("Work: startTime: " + request.getStartTime() + " endTime:" + request.getEndTime());
				workerRouter.tell(new Work(start, msg, request.getStartTime(), request.getEndTime()), getSelf());
				++start;
			}
		} else if (message instanceof Work) {
			Work result = (Work) message;
			filteredMessages.add(result.getUnfilteredMessage());
			nrOfResults += 1;
			if (nrOfResults == numberOfMessages) {
				//Duration duration = Duration.create(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);				
				listener.tell(new Result(filteredMessages, System.currentTimeMillis() - start), getSelf());
				getContext().stop(getSelf());
			}
		} else {
			unhandled(message);
		}
	}
}
