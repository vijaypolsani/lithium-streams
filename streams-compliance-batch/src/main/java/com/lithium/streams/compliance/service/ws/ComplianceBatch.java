package com.lithium.streams.compliance.service.ws;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

public interface ComplianceBatch {

	/**
	 * API to get the last Event Unique SequenceId SET delivered to the Consumer. The startID & endID are required
	 * @param communityName
	 * @param login
	 * @return String Last event UniqueID of the content supplied to the customer
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public abstract Response getBatchLatestSequenceId(String clientId, String startId, String endId)
			throws InterruptedException, ExecutionException;

	/**
	 * API to get the last Event TimeStamp delivered to the Consumer. This helps in tracking the consumption of the client.
	 * @param communityName
	 * @param login
	 * @return String Last event timestamp of the content supplied to the customer
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */

	public abstract Response getBatchLatestEndTime(String clientId, String startTime, String endTime)
			throws InterruptedException, ExecutionException;

}