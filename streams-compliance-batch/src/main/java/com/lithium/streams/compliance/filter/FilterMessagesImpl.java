package com.lithium.streams.compliance.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.lithium.streams.compliance.api.FilterMessages;
import com.lithium.streams.compliance.model.ComplianceMessage;

public class FilterMessagesImpl implements FilterMessages {
	public static final Logger log = LoggerFactory.getLogger(FilterMessagesImpl.class);

	@Autowired
	private FilteringSystem filteringSystem;

	@Override
	public Collection<ComplianceMessage> receiveMessages(Collection<ComplianceMessage> unfilteredMessages,
			long startTime, long endTime) {
		log.info(">>>Typed Actor, Calling FilterSystem: ");
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));

		Future<Object> future = Patterns.ask(filteringSystem.getMaster(), new Request(unfilteredMessages, startTime,
				endTime), timeout);
		try {
			Result result = (Result) Await.result(future, timeout.duration());
			log.info("Result: " + result.getMessages().size());
			return result.getMessages();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<ComplianceMessage>();
	}
}
