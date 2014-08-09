package com.lithium.streams.compliance.util;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import com.lithium.streams.compliance.model.ComplianceMessage;

final public class BlockingQueueOfOne {

	private static final BlockingQueue<Collection<ComplianceMessage>> queue = new LinkedTransferQueue<>();

	/**
	 * @return the queue
	 */
	public static Collection<ComplianceMessage> getQueue() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void putQueue(Collection<ComplianceMessage> msg) {
		try {
			queue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
