package com.lithium.streams.compliance.api;

import java.util.Collection;

import kafka.javaapi.FetchResponse;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;

import com.lithium.streams.compliance.model.ComplianceMessage;

public interface KafkaLowLevelApi {

	/**
	 *  Get the List of Offsets for a given topic.
	 *  @param topicName object.
	 *  @return offset List containing offset number.
	 * 	@throws Exception 
	 */
	public abstract long[] getOffsetsOfTopic(String topicName) throws Exception;

	/**
	 *  Get the Latest Offset for a given topic.
	 *  @param topicName object.
	 *  @return offset number containing offset number.
	 * 	@throws Exception 
	 */
	public abstract long getEarliestOffsetOfTopic(String topicName) throws Exception;

	/**
	 *  Get the Oldest Offset for a given topic.
	 *  @param topicName object.
	 *  @return offset number containing offset number.
	 * 	@throws Exception 
	 */
	public abstract long getLatestOffsetOfTopic(String topicName) throws Exception;

	/**
	 *  Commit the offsets of all topic/partitions connected by this connector.
	 */
	public abstract void commitOffsets();

	/**
	 *  Fetch metadata of a topics.
	 *  @param request TopicMetadataRequest.
	 *  @return TopicMetadataResponse for each topic in the request.
	 * @throws Exception 
	 */
	public abstract TopicMetadataResponse send(TopicMetadataRequest topicMeta) throws Exception;

	
	/**
	 *  Prepare the GetchResponse for a give TopicName & Offset
	 */
	public abstract FetchResponse getFetchResponse(String topicName, long readOffset) throws Exception;
	
}
