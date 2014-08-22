package com.lithium.streams.compliance.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateFormatterTest {
	private static final String SAMPLE_DATE = "2013-08-01T20:10:00.000Z";

	@Test
	public void testDateParsing() {
		long returnData = DateFormatter.formatDate(SAMPLE_DATE);
		System.out.println(">>>Return Data: "+returnData);
		assertEquals(new Long(1375387800).longValue(), returnData);
	}
	//2000-04-01T22:10:00.000Z
	//2014-08-01T21:10:48.000Z
	@Test
	public void testDateParsing1() {
		long returnData = DateFormatter.formatDate("2000-04-01T22:10:00.000Z");
		System.out.println(">>>Return Data: "+returnData);
		assertEquals(new Long(954627000).longValue(), returnData);
	}
	@Test
	public void testDateParsing2() {
		long returnData = DateFormatter.formatDate("2014-08-01T21:10:48.000Z");
		System.out.println(">>>Return Data: "+returnData);
		assertEquals(new Long(1406927448).longValue(), returnData);
	}
}
