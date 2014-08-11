package com.lithium.streams.compliance.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinPool;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.lithium.streams.compliance.model.ComplianceMessage;

public class Master extends UntypedActor {
	private static final Logger log = LoggerFactory.getLogger(Master.class);
	private static final long start = System.currentTimeMillis();
	private final int nrOfWorkers;
	long nrOfResults = 0L;
	long numberOfMessages = 0L;
	final Collection<ComplianceMessage> filteredMessages = new ArrayList<>();

	private final ActorRef listener;
	private final ActorRef workerRouter;
	private AtomicReference<ActorRef> originator = new AtomicReference<ActorRef>();

	public Master(int nrOfWorkers, ActorRef listener) {
		this.listener = listener;
		this.nrOfWorkers = nrOfWorkers;
		this.workerRouter = this.getContext().actorOf(
				Props.create(Worker.class).withRouter(new RoundRobinPool(nrOfWorkers)));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Request) {
			Request request = (Request) message;
			int start = 0;
			numberOfMessages = request.getUnfilteredMessages().size();
			log.info("Work: Number Of Messages: " + numberOfMessages);
			log.info("Work: startTime: " + request.getStartTime() + " endTime:" + request.getEndTime());
			log.info(">>>Sender in Request: " + getSender());
			originator.set(getSender());
			for (ComplianceMessage msg : request.getUnfilteredMessages()) {
				workerRouter.tell(new Work(start, msg, request.getStartTime(), request.getEndTime()), getSelf());
				++start;
			}
		} else if (message instanceof WorkResponse) {
			WorkResponse workResponse = (WorkResponse) message;
			if (workResponse.isMatching())
				filteredMessages.add(workResponse.getUnfilteredMessage());
			++nrOfResults;
			if (nrOfResults == numberOfMessages) {
				log.info(">>>Work: Response: " + filteredMessages.size() + " Time: "
						+ (System.currentTimeMillis() - start));
				ImmutableCollection<ComplianceMessage> immutableCollection = ImmutableList.copyOf(filteredMessages);
				//listener.tell(new Result(ImmutableList.copyOf(immutableCollection),(System.currentTimeMillis() - start)), getSelf());
				log.debug(">>>Sender in Response: " + getSender());
				log.debug(">>>Originator: " + originator.toString());
				originator.get().tell(
						new Result(ImmutableList.copyOf(immutableCollection), (System.currentTimeMillis() - start)),
						getContext().self());
				//getSelf().tell(new Result(ImmutableList.copyOf(immutableCollection), (System.currentTimeMillis() - start)),ActorRef.noSender());
				//getContext().parent().tell(new Result(ImmutableList.copyOf(immutableCollection), (System.currentTimeMillis() - start)),ActorRef.noSender());

				//Reset
				filteredMessages.clear();
				nrOfResults = 0L;
				numberOfMessages = 0L;
				//NOTE: Don't close the context.
				//getContext().stop(getSelf());
			}
		} else {
			log.error("<<< Work: Response: Unhandled Message" + message.getClass());

			unhandled(message);
		}
	}
}
