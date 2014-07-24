package com.lithium.streams.compliance.api;

import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.Response;

public interface ComplianceBatchEvents extends KafkaLowLevelApi {

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