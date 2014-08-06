package com.lithium.streams.compliance.filter;

import akka.actor.UntypedActor;

public class FilterResponseListener extends UntypedActor {

	public FilterResponseListener() {
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Result) {

			Result result = (Result) message;
			System.out.println("Filtered List: Size = " + result.getMessages().size() + " Total Time Taken: "
					+ result.getDuration());
			//getContext().system().shutdown();
		} else {
			unhandled(message);
		}

	}
}
