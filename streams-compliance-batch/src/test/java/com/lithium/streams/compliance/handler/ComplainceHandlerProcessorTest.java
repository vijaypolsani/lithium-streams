package com.lithium.streams.compliance.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lithium.streams.compliance.api.ComplianceEvent;
import com.lithium.streams.compliance.model.ComplianceMessage;

public class ComplainceHandlerProcessorTest {

	@Before
	public void setup() throws Exception {
	}

	@Test
	public void processorTest() {
		ComplainceHandlerProcessor complainceHandlerProcessor = new ComplainceHandlerProcessor();
		complainceHandlerProcessor.printHandlerChain();
		ComplianceMessage complianceMessage = ComplianceMessage.MsgBuilder.init("test").build();
		Assert.assertNotNull(complainceHandlerProcessor.processChain(complianceMessage));
	}
}
