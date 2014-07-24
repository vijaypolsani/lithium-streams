package com.lithium.streams.compliance.service;

import java.util.ArrayList;
import java.util.List;

import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.consumer.SimpleConsumer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:./spring/appContext.xml")
@ActiveProfiles("dev")
public class TestKafkaConsumer {

	private static kafka.javaapi.TopicMetadataRequest topicMetaDataReqeuest;
	private static kafka.javaapi.FetchResponse fetchResponse;
	private static final List<String> topicList = new ArrayList<>();
	private static final String TOPIC_NAME = "lia";
	private static final String TOPIC_NAME_ACTIANCE = "actiance.stage";
	private static final SimpleConsumer simpleConsumer = new SimpleConsumer("10.240.163.94", 9092, 100000, 64 * 1024,
			"junit");;

	@Autowired
	private ComplianceBatchStandalone complianceBatchStandalone;

	@Before
	public void setup() {
		topicList.add(TOPIC_NAME);
		topicMetaDataReqeuest = new kafka.javaapi.TopicMetadataRequest(topicList);

	}

	@After
	public void trearDown() {
		topicMetaDataReqeuest = null;
	}

	public TestKafkaConsumer() {
	}

	@Test
	public void getKafkaGetMetadataInformation() {
		kafka.javaapi.TopicMetadataResponse topicMetaDataResponse;

		topicMetaDataResponse = simpleConsumer.send(topicMetaDataReqeuest);
		Assert.notNull(topicMetaDataResponse);
		List<TopicMetadata> metaData = topicMetaDataResponse.topicsMetadata();
		for (TopicMetadata item : metaData) {
			for (PartitionMetadata part : item.partitionsMetadata()) {
				System.out.println(" Data from Metadata, leader: " + part.leader().toString());
				System.out.println(" Data from Metadata, partitionId : " + part.partitionId());
				System.out.println(" Data from Metadata, replicas: " + part.replicas());
			}
		}
	}

	@Test
	public void getLastOffset() throws Exception {
		long offset = complianceBatchStandalone.getLatestOffsetOfTopic(TOPIC_NAME);
		System.out.println(" LastOffset offset: " + offset);
		Assert.notNull(offset);
	}

	@Test
	public void getEarliestOffset() throws Exception {
		//kafka.api.OffsetRequest.EarliestTime()
		long offset = complianceBatchStandalone.getEarliestOffsetOfTopic(TOPIC_NAME);
		System.out.println(" EarliestOffset offset: " + offset);
		Assert.notNull(offset);
	}

}
