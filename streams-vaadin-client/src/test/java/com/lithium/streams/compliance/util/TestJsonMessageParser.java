package com.lithium.streams.compliance.util;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.lithium.streams.compliance.model.ActivityStreamv1;

public class TestJsonMessageParser {

	public static final String SAMPLE_DATA = "{\"event\":{\"id\":244806492,\"type\":\"Blackbox\",\"time\":1417632301553,\"frameId\":244806491,\"version\":\"14.10.0\",\"service\":\"lia\",\"source\":\"actiance.stage\",\"node\":\"2E8225CA\"},\"payload\":{\"line\":\"stats 1417632301553 0 1251162 0 0 0 - 0 0 0\"}}";

	@Test
	public void testJsonMessageParser() {
		ActivityStreamv1 activityStreamv1 = null;
		try {
			activityStreamv1 = JsonMessageParser.parseIncomingsJsonStreamToObject(SAMPLE_DATA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(activityStreamv1.toString());
		assertNotNull(activityStreamv1);
	}
}
