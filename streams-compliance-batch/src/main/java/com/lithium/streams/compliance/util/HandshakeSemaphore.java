package com.lithium.streams.compliance.util;

import java.util.concurrent.Semaphore;

public class HandshakeSemaphore {
	private static final Semaphore available = new Semaphore(1, true);

	public static boolean getLock() {
		try {
			available.acquire();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void releaseLock() {
		available.release();
	}
}
