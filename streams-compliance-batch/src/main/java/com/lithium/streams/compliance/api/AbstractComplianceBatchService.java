package com.lithium.streams.compliance.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.lithium.streams.compliance.consumer.EnumKafkaProperties;
import com.lithium.streams.compliance.consumer.KafkaSimpleConsumerFactory;
import com.lithium.streams.compliance.consumer.SimpleConsumerPool;
import com.lithium.streams.compliance.handler.ComplainceHandlerProcessor;
import com.lithium.streams.compliance.model.ComplianceHeader;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.model.CompliancePayload;

public abstract class AbstractComplianceBatchService implements KafkaLowLevelApi {

	private static final Logger log = LoggerFactory.getLogger(AbstractComplianceBatchService.class);
	private static final List<String> topicList = new ArrayList<String>();
	private final TopicMetadataRequest topicMetaDataReqeuest;
	private final SimpleConsumerPool simpleConsumerPool;

	protected static KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory;

	@Autowired
	private ComplainceHandlerProcessor complainceHandlerProcessor;

	public AbstractComplianceBatchService(KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory) {
		topicList.add(EnumKafkaProperties.TOPIC_ACTIANCE.getKafkaProperties());
		topicMetaDataReqeuest = new kafka.javaapi.TopicMetadataRequest(topicList);
		simpleConsumerPool = kafkaSimpleConsumerFactory.getPool();
	}

	@Override
	public long getLatestOffsetOfTopic(String topicName) throws Exception {
		log.info(">>> Calling get Current Offset");
		TopicAndPartition topicAndPartition = new TopicAndPartition(topicName, Integer
				.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties()));
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.LatestTime(), 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest
				.CurrentVersion(), EnumKafkaProperties.CLIENT_NAME.getKafkaProperties());
		SimpleConsumer consumer = simpleConsumerPool.borrowObject();
		OffsetResponse response = consumer.getOffsetsBefore(request);

		if (response.hasError()) {
			System.out.println("<<< Error fetching data Offset Data the Broker. Reason: "
					+ response.errorCode(topicName, Integer
							.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties())) + " Exception Response: "
					+ response.toString());
			return 0;
		}
		long[] offsets = response.offsets(topicName, Integer.parseInt(EnumKafkaProperties.PARTITION
				.getKafkaProperties()));
		simpleConsumerPool.returnObject(consumer);
		log.info(">>> From Kafka Current Offset:" + offsets[0]);
		return offsets[0];
	}

	@Override
	public long getEarliestOffsetOfTopic(String topicName) throws Exception {
		log.info(">>> Calling get Oldest Offset");
		TopicAndPartition topicAndPartition = new TopicAndPartition(topicName, Integer
				.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties()));
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.EarliestTime(), 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest
				.CurrentVersion(), EnumKafkaProperties.CLIENT_NAME.getKafkaProperties());
		SimpleConsumer consumer = simpleConsumerPool.borrowObject();
		OffsetResponse response = consumer.getOffsetsBefore(request);

		if (response.hasError()) {
			System.out.println("<<< Error fetching data Offset Data the Broker. Reason: "
					+ response.errorCode(topicName, Integer
							.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties())) + " Exception Response: "
					+ response.toString());
			return 0;
		}
		long[] offsets = response.offsets(topicName, Integer.parseInt(EnumKafkaProperties.PARTITION
				.getKafkaProperties()));
		simpleConsumerPool.returnObject(consumer);
		log.info(">>> From Kafka Oldest Offset:" + offsets[0]);
		return offsets[0];
	}

	@Override
	public long[] getOffsetsOfTopic(String topicName) throws Exception {
		log.info(">>> Calling get List of Offset from Topic.");
		long[] offsets = null;
		TopicAndPartition topicAndPartition = new TopicAndPartition(topicName, Integer
				.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties()));
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(kafka.api.OffsetRequest.EarliestTime(), 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest
				.CurrentVersion(), EnumKafkaProperties.CLIENT_NAME.getKafkaProperties());
		SimpleConsumer consumer = simpleConsumerPool.borrowObject();
		OffsetResponse response = consumer.getOffsetsBefore(request);

		if (response.hasError()) {
			System.out.println("<<< Error fetching data Offset Data the Broker. Reason: "
					+ response.errorCode(topicName, Integer
							.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties())) + " Exception Response: "
					+ response.toString());
			return offsets;
		}
		offsets = response.offsets(topicName, Integer.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties()));
		simpleConsumerPool.returnObject(consumer);
		log.info(">>> From Kafka Offsets: " + offsets);

		return offsets;
	}

	@Override
	public void commitOffsets() {
		log.info("<<< Not implmented Yet.");
	}

	@Override
	public TopicMetadataResponse send(TopicMetadataRequest topicMeta) throws Exception {
		kafka.javaapi.TopicMetadataResponse topicMetaDataResponse;
		SimpleConsumer consumer = simpleConsumerPool.borrowObject();
		topicMetaDataResponse = consumer.send(topicMetaDataReqeuest);
		Assert.notNull(topicMetaDataResponse);
		List<TopicMetadata> metaData = topicMetaDataResponse.topicsMetadata();
		for (TopicMetadata item : metaData) {
			for (PartitionMetadata part : item.partitionsMetadata()) {
				log.info(" Data from Metadata, leader: " + part.leader().toString());
				log.info(" Data from Metadata, partitionId : " + part.partitionId());
				log.info(" Data from Metadata, replicas: " + part.replicas());
			}
		}
		simpleConsumerPool.returnObject(consumer);
		return topicMetaDataResponse;
	}

	@Override
	public Collection<ComplianceMessage> processStream(final String topicName) throws Exception {
		log.info(">>> Process Stream from Kafka for Topic: " + topicName);
		long readOffset = getEarliestOffsetOfTopic(topicName);
		List<ComplianceMessage> returnMessages = new ArrayList<>();
		FetchResponse fetchResponse = getFetchResponse(topicName, readOffset);
		for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topicName, Integer
				.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties()))) {
			long currentOffset = messageAndOffset.offset();
			if (currentOffset < readOffset) {
				log.info("Found an old offset: " + currentOffset + " Expecting: " + readOffset);
				continue;
			}
			readOffset = messageAndOffset.nextOffset();
			ByteBuffer payload = messageAndOffset.message().payload();

			byte[] bytes = new byte[payload.limit()];
			payload.get(bytes);
			CompliancePayload compliancePayload = new CompliancePayload(new String(bytes, "UTF-8"));
			log.info(">>> Offset:Data: " + String.valueOf(messageAndOffset.offset()) + ": "
					+ compliancePayload.getJsonMessage());
			ComplianceMessage complianceMessage = new ComplianceMessage.MsgBuilder(topicName + ":"
					+ messageAndOffset.offset()).header(
					new ComplianceHeader.HeaderBuilder(System.currentTimeMillis()).communityId(topicName).build())
					.payload(compliancePayload).build();
			log.info(">>> Calling Processor:" + complainceHandlerProcessor.printHandlerChain());
			complainceHandlerProcessor.processChain(complianceMessage);
			log.info(">>> Completed Processor Chain Calls.");
			returnMessages.add(complianceMessage);
		}
		return returnMessages;
	}

	private FetchResponse getFetchResponse(final String topicName, long readOffset) throws Exception {
		FetchRequest req = new FetchRequestBuilder().clientId(EnumKafkaProperties.CLIENT_NAME.getKafkaProperties())
				.addFetch(topicName, Integer.parseInt(EnumKafkaProperties.PARTITION.getKafkaProperties()), readOffset,
						1024000) // Note: this fetchSize of 100000 might need to be increased if large batches are written to Kafka
				.build();
		SimpleConsumer consumer = simpleConsumerPool.borrowObject();
		FetchResponse fetchResponse = consumer.fetch(req);
		int numErrors = 0;
		if (fetchResponse.hasError()) {
			numErrors++;
			// Something went wrong!
			short code = fetchResponse.errorCode(topicName, Integer.parseInt(EnumKafkaProperties.PARTITION
					.getKafkaProperties()));
			System.out.println("Error fetching data from the Broker:" + 0 + " Reason: " + code);
			if (numErrors > 5)
				return null;
			if (code == ErrorMapping.OffsetOutOfRangeCode()) {
				// We asked for an invalid offset. For simple case ask for the last element to reset
				readOffset = getEarliestOffsetOfTopic(topicName);
			}
			simpleConsumerPool.returnObject(consumer);
		}
		simpleConsumerPool.returnObject(consumer);
		return fetchResponse;
	}

	public static void setKafkaSimpleConsumerFactory(KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory) {
		AbstractComplianceBatchService.kafkaSimpleConsumerFactory = kafkaSimpleConsumerFactory;
	}

	public void setComplainceHandlerProcessor(ComplainceHandlerProcessor complainceHandlerProcessor) {
		this.complainceHandlerProcessor = complainceHandlerProcessor;
	}

}
