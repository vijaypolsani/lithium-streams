package com.lithium.streams.compliance.util;

import static com.google.common.io.BaseEncoding.base16;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import lithium.research.key.KeySource;
import lithium.research.keys.ClientKeySource;

import org.apache.log4j.BasicConfigurator;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.model.KeySourceHolder;

public class TestFixedSizeSortedSet {
	private static final Logger log = LoggerFactory.getLogger(TestFixedSizeSortedSet.class);

	private static FixedSizeSortedSet<KeySourceHolder> twoSizeSortedSet;
	private static FixedSizeSortedSet<KeySourceHolder> fiveSizeSortedSet;
	private static KeySourceUtil keySourceUtil;
	private Optional<KeySource> source;
	private KeySourceHolder keySourceHolder;

	@Before
	public void setUp() {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		twoSizeSortedSet = new FixedSizeSortedSet<KeySourceHolder>(new KeySourceComparator(), 2,"/Users/vijay.polsani/temp/host.key");
		fiveSizeSortedSet = new FixedSizeSortedSet<KeySourceHolder>(new KeySourceComparator(), 5,"/Users/vijay.polsani/temp/host.key");
		keySourceUtil = EasyMock.createMock(KeySourceUtil.class);

	}

	@After
	public void tearDown() {
		twoSizeSortedSet = null;
		keySourceUtil = null;
		fiveSizeSortedSet = null;
	}

	private Optional<KeySource> createMockKeySource() {
		Optional<KeySource> source = null;
		try {
			log.info("Current Directory: "
					+ getClass().getProtectionDomain().getCodeSource().getLocation().toExternalForm());
			source = Optional.of(new ClientKeySource(URI.create(MockKeyServerProperties.URI_LINK.getValue()),
					MockKeyServerProperties.EMAIL.getValue(), new SecretKeySpec(base16().decode(
							MockKeyServerProperties.USER_KEY.getValue()), MockKeyServerProperties.AES.getValue()),
					getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
							+ MockKeyServerProperties.HOST_KEY_PATH.getValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return source;
	}

	@Test
	public void test2SizeSortedSet10Times() {
		try {
			EasyMock.expect((keySourceUtil.getKeySource())).andReturn(createMockKeySource()).times(10);
			EasyMock.replay(keySourceUtil);
			for (int i = 5; i > 0; i--) {
				Optional<KeySource> source = keySourceUtil.getKeySource();
				keySourceHolder = new KeySourceHolder(source);
				boolean ableToAdd = twoSizeSortedSet.add(keySourceHolder);
				log.info("--- New KeySource added to Stack: " + ableToAdd);
				twoSizeSortedSet.printCollection();
				Thread.sleep(1000);
				assertTrue(ableToAdd);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test5SizeSortedSet10Times() {

		try {
			EasyMock.expect((keySourceUtil.getKeySource())).andReturn(createMockKeySource()).times(10);
			EasyMock.replay(keySourceUtil);
			Optional<KeySource> source = keySourceUtil.getKeySource();
			for (int i = 5; i > 0; i--) {
				keySourceHolder = new KeySourceHolder(source);
				boolean ableToAdd = fiveSizeSortedSet.add(keySourceHolder);
				log.info("--- New KeySource added to Stack: " + ableToAdd);
				fiveSizeSortedSet.printCollection();
				Thread.sleep(1000);
				assertTrue(ableToAdd);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getKeySource() {
		try {
			EasyMock.expect((keySourceUtil.getKeySource())).andReturn(createMockKeySource()).times(10);
			EasyMock.replay(keySourceUtil);
			Optional<KeySource> source = keySourceUtil.getKeySource();
			for (int i = 5; i > 0; i--) {
				log.info("--- Adding a new KeySource to Holder: " + source);
				keySourceHolder = new KeySourceHolder(source);
				log.info("--- New KeySource added to Stack: " + twoSizeSortedSet.add(keySourceHolder));
				log.info("--- Current Location: " + getClass().getResource("/conf/host.key"));
				log.info("--- Get KeySource for Keys: " + twoSizeSortedSet.getKeySource().get());
				Thread.sleep(1000);
				assertNotNull(twoSizeSortedSet.getKeySource());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getBackupKeySource() {
		try {
			EasyMock.expect((keySourceUtil.getKeySource())).andReturn(createMockKeySource()).times(1).andReturn(
					createMockKeySource()).times(1).andReturn(createMockKeySource()).times(100);
			EasyMock.replay(keySourceUtil);
			for (int i = 5; i > 0; i--) {
				log.info("--- Adding a new KeySource to Holder: " + keySourceUtil.getKeySource());
				keySourceHolder = new KeySourceHolder(keySourceUtil.getKeySource());
				log.info("--- New KeySource added to Stack: " + twoSizeSortedSet.add(keySourceHolder));

				Optional<KeySource> previousKeySource = twoSizeSortedSet.getKeySource(keySourceHolder);
				if (previousKeySource.isPresent()) {
					log.info("--- Get KeySource for Keys: " + previousKeySource.get());
					assertNotNull(previousKeySource.get());
				} else {
					log.info("--- Get KeySource for Keys: DS does not have lower key: " + previousKeySource);
					assertEquals(Optional.empty(), previousKeySource);
				}

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
