package com.lithium.streams.compliance.filter;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Test;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.lithium.streams.compliance.api.MessageFilteringService;
import com.lithium.streams.compliance.model.ComplianceMessage;
import com.lithium.streams.compliance.model.CompliancePayload;

public class FilterSystemTest {

	private static final Logger log = Logger.getLogger(FilterSystemTest.class);

	static {
		BasicConfigurator.configure();
	}

	@Test
	public void testTheFilterCapabilitiesFor10000() {
		//1 year
		long startTime = System.currentTimeMillis() - 99999999999l;

		long endTime = System.currentTimeMillis();
		log.info("Start Time : " + startTime);
		log.info("End Time : " + endTime);

		Collection<ComplianceMessage> messages = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			ComplianceMessage msg = ComplianceMessage.MsgBuilder.init("junit" + i).payload(
					CompliancePayload.init(MessageFilteringService.SAMPLE_INPUT1.getBytes())).build();
			messages.add(msg);
		}
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));

		FilteringSystem filteringSystem = new FilteringSystem();
		Future<Object> future = Patterns.ask(filteringSystem.getMaster(), new Request(messages, startTime, endTime),
				timeout);
		Result result = null;
		try {
			result = (Result) Await.result(future, timeout.duration());
			log.info("Result: " + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(result);

	}
}
