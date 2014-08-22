package com.lithium.streams.compliance.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.lithium.streams.compliance.api.AbstractComplianceBatchService;
import com.lithium.streams.compliance.api.ComplianceBatchEvents;
import com.lithium.streams.compliance.consumer.KafkaSimpleConsumerFactory;
import com.lithium.streams.compliance.filter.FilteringSystem;
import com.lithium.streams.compliance.filter.Request;
import com.lithium.streams.compliance.filter.Result;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.util.BatchOperations;
import com.lithium.streams.compliance.util.DateFormatter;

public class ComplianceBatchStandalone extends AbstractComplianceBatchService implements ComplianceBatchEvents {
	private static final Logger log = LoggerFactory.getLogger(ComplianceBatchStandalone.class);
	private static final int TIME_OUT = 10; // Sec
	@Autowired
	private FilteringSystem filteringSystem;

	public ComplianceBatchStandalone(KafkaSimpleConsumerFactory kafkaSimpleConsumerFactory) {
		super(kafkaSimpleConsumerFactory);
		log.info(">>> Called Super for KafkaConnectionPool and Data Discovery.");

	}

	@Override
	public Collection<ComplianceMessage> processStream(String topicName) throws Exception {
		checkNotNull(topicName, "Input Topic/Community name cannot be null.");
		return super.process(new ComplianceContext(topicName, null, null), BatchOperations.ALL);
	}

	@Override
	public Collection<ComplianceMessage> getMessagesFilteredByTime(String topicName, String start, String end)
			throws Exception {
		checkNotNull(topicName, "Input TopicName/CommunityName cannot be null.");
		checkNotNull(start, "Input Start Time cannot be null.");
		checkNotNull(end, "Input Start Time cannot be null.");
		log.info(">>>Input Parameters Community: " + topicName);
		log.info(">>>Input Parameters Start Time: " + start);
		log.info(">>>Input Parameters End Time: " + end);
		final Collection<ComplianceMessage> unfilteredMessages = super.process(new ComplianceContext(topicName, null,
				null), BatchOperations.FILTER_BY_TIME);
		return callFilter(unfilteredMessages, start, end);
	}

	private synchronized Collection<ComplianceMessage> callFilter(
			final Collection<ComplianceMessage> unfilteredMessages, final String startTime, final String endTime) {
		log.info(">>>Calling Filter process.");
		long start = System.currentTimeMillis();
		// Direct Call with Listener (onReceive).
		//filteringSystem.filter(unfilteredMessages, startTime, endTime);

		//Sync Call with local Listener.
		Timeout t = new Timeout(TIME_OUT, TimeUnit.SECONDS);
		long startT = DateFormatter.formatDate(startTime);
		log.info(">>>Start Time: " + startT);
		long endT = DateFormatter.formatDate(endTime);
		log.info(">>>End Time: " + endT);
		Future<Object> fut = Patterns
				.ask(filteringSystem.getMaster(), new Request(unfilteredMessages, startT, endT), t);
		Result result = null;
		try {
			result = (Result) Await.result(fut, t.duration());
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ComplianceMessage>();
		}
		log.info(">>>Result from Akka Actor. Time Taken: ms " + (System.currentTimeMillis() - start) + " Result Size"
				+ result.getMessages().size());
		return result.getMessages();
		//return BlockingQueueOfOne.getQueue();
	}
}
