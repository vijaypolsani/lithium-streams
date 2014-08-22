package com.lithium.streams.compliance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateFormatter {
	private final static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");//2013-08-01T20:10:00.000Z
	private final static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");//2013-08-01T20:10:00.000Z
	private static final Logger log = LoggerFactory.getLogger(DateFormatter.class);

	public static long formatDate(String inputStringDate) {
		try {
			return Instant.parse(inputStringDate).getEpochSecond();
		} catch (DateTimeParseException dte) {
			log.info(">>>Input messages does not comply with expected format. " + "yyyy-MM-ddTHH:mm:ss.SSSZ");
			try {
				Date date = dateFormat2.parse(inputStringDate);
				return date.getTime();
			} catch (ParseException e) {
				log.info(">>>Input messages does not comply with expected format. " + "yyyy-MM-ddTHH:mm");
			}
			return Long.parseLong(inputStringDate);
		}
	}
}
