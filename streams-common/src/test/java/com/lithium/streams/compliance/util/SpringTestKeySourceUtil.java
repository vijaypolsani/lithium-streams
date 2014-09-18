package com.lithium.streams.compliance.util;

import static org.junit.Assert.*;
import lithium.research.key.KeySource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lithium.streams.compliance.model.KeySourceHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/commonContext.xml" })
public class SpringTestKeySourceUtil {
	private static final Logger log = LoggerFactory.getLogger(SpringTestKeySourceUtil.class);

	@BeforeClass
	public static void init() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
	}

	@Autowired
	public KeySourceUtil keySourceUtil;

	@Test
	public void testKeySourceUtil() {
		log.info(">>>Bean Created. Checking KeySource isPresent?: " + keySourceUtil.getKeySource().get());
		keySourceUtil.refreshKeySourceStash();
		log.info(">>>Store Refresh Completed. ");
		KeySourceHolder keySourceHolder = keySourceUtil.getKeySourceHolder();
		KeySource source = keySourceUtil.getKeySource().get();
		log.info(">>>Getting KeySource. " + source);
		log.info(">>>Getting KeySourceHolder. " + keySourceUtil.getKeySourceHolder());
		if (keySourceUtil.getOlderKeySource(keySourceHolder).isPresent()) {
			KeySource sourceOlder = keySourceUtil.getOlderKeySource(keySourceHolder).get();
			log.info(">>>Getting Older KeySource. " + sourceOlder);
		} else
			log.info(">>>Getting Older KeySource does not exist.");
		keySourceUtil.listKeyStore();
		assertNotNull(keySourceUtil);
	}
}
