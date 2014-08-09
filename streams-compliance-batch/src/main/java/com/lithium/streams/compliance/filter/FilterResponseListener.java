package com.lithium.streams.compliance.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.UntypedActor;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;

import com.lithium.streams.compliance.util.BlockingQueueOfOne;

public class FilterResponseListener extends UntypedActor implements RequiresMessageQueue<BoundedMessageQueueSemantics> {
	private static final Logger log = LoggerFactory.getLogger(FilterResponseListener.class);

	public FilterResponseListener() {
	}

	@Override
	public void preStart() {
		//getContext().setReceiveTimeout(Duration.create(TIMEOUT));
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Result) {
			Result result = (Result) message;
			log.info(">>>FilterResponseListener List: Size = " + result.getMessages().size() + " Total Time Taken: "
					+ result.getDuration());
			BlockingQueueOfOne.putQueue(result.getMessages());
		} else {
			unhandled(message);
		}

	}
}
