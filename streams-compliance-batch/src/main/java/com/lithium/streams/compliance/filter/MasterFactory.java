package com.lithium.streams.compliance.filter;

import akka.actor.ActorRef;
import akka.japi.Creator;

public class MasterFactory implements Creator<Master> {
	private final int nrOfWorkers;
	private final ActorRef listener;

	public MasterFactory(int nrOfWorkers, ActorRef listener) {
		this.nrOfWorkers = nrOfWorkers;
		this.listener = listener;
	}

	public Master create() {
		return new Master(nrOfWorkers, listener);
	}
}