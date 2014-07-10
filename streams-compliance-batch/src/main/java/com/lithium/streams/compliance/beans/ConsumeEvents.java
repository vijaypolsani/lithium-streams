package com.lithium.streams.compliance.beans;

import java.math.BigInteger;
import java.time.Clock;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;

public interface ConsumeEvents {

	public abstract void getBatchEventsBySequenceId(BigInteger startId, BigInteger endId);

	public abstract void getBatchEventsByTimeId(BigInteger startId, BigInteger endId);

	public static ZoneId getTimeZone(@NotNull String zone) {
		return ZoneId.of("America/Los_Angeles");
	}

	public static Clock getTimeZone() {
		return Clock.system(ZoneId.of("America/Los_Angeles"));
	}
}
