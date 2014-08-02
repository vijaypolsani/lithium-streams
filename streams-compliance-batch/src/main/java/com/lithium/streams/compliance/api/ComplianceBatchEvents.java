package com.lithium.streams.compliance.api;

import java.util.Collection;

import com.lithium.streams.compliance.model.ComplianceMessage;

public interface ComplianceBatchEvents extends KafkaLowLevelApi {

	/**
	 * API to get the last Event TimeStamp delivered to the Consumer. This helps in tracking the consumption of the client.
	 * @param communityName or topicName - Both are same
	 * @param startTime start time
	 * @param endTime end time
	 * @return Collection Compliance Events
	 * @throws Execution exception
	 */

	public abstract Collection<ComplianceMessage> getMessagesFilteredByTime(String topicName, String startTime,
			String endTime) throws Exception;

	/**
	 * API to get all the events for the Consumer. 
	 * @param communityName or topicName - Both are same
	 * @return Collection Compliance Events
	 * @throws Execution exception
	 */
	public abstract Collection<ComplianceMessage> processStream(String topicName) throws Exception;

	/**
	 * API to get the last Event Unique SequenceId SET delivered to the Consumer. The startID & endID are required
	 * @param communityName or topicName - Both are same
	 * @param startId start sequence id
	 * @param endId end sequence id
	 * @return Collection Compliance Events
	 * @throws Execution exception
	 */
	public default Collection<ComplianceMessage> getMessagesFilteredById(String topicName, String startId, String endId)
			throws Exception {
		throw new UnsupportedOperationException();
	}

}