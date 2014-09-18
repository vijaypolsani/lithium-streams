package com.lithium.streams.compliance.util;

import static com.google.common.io.BaseEncoding.base16;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeSet;

import javax.crypto.spec.SecretKeySpec;

import lithium.research.key.KeySource;
import lithium.research.keys.ClientKeySource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedSizeSortedSet<KeySourceHolder> extends TreeSet<KeySourceHolder> {

	private static final long serialVersionUID = 4133039913231315811L;
	private final Comparator<KeySourceHolder> comparator;
	private final int cachedKeySourceLimit;

	private static final Logger log = LoggerFactory.getLogger(FixedSizeSortedSet.class);

	public FixedSizeSortedSet(Comparator<KeySourceHolder> comparator, int cachedKeySourceLimit) {
		super(Collections.synchronizedSortedSet(new TreeSet<KeySourceHolder>(comparator)));
		this.comparator = comparator;
		this.cachedKeySourceLimit = cachedKeySourceLimit;
	}

	public Optional<KeySource> createKeySource() {
		Optional<KeySource> source = null;
		try {
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

	@Override
	public boolean add(KeySourceHolder keySourceHolder) {
		if (size() >= cachedKeySourceLimit) {
			KeySourceHolder smallest = first();
			int comparision = comparator.compare(keySourceHolder, smallest);
			if (comparision > 0) {
				remove(smallest);
				log.info(">>> Older KeySource timestamp removed: "
						+ ((com.lithium.streams.compliance.model.KeySourceHolder) smallest).getTimeEpochMilli());
				log.info(">>> Newly Added KeySource timestamp added: "
						+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
				return super.add(keySourceHolder);
			}
			return false;
		} else {
			log.info(">>> New Key found & added: "
					+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
			return super.add(keySourceHolder);
		}
	}

	protected Optional<KeySource> getKeySource() {
		KeySourceHolder keySourceHolder = super.last();
		log.info(">>>Most recent KeySource in the DS: "
				+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
		return ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getKeysource();
	}

	protected KeySourceHolder getKeySourceHolder() {
		KeySourceHolder keySourceHolder = super.last();
		log.info(">>>Most recent KeySourceHolder in the DS: "
				+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
		return keySourceHolder;
	}

	protected Optional<KeySource> getKeySource(KeySourceHolder currentKeySourceHolder) {
		KeySourceHolder keySourceHolder = super.lower(currentKeySourceHolder);
		if (keySourceHolder == null)
			return Optional.empty();
		log.info(">>>Next to most recent KeySource in the DS:"
				+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
		return ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getKeysource();
	}

	protected KeySourceHolder getOlderKeySourceHolder(KeySourceHolder currentKeySourceHolder) {
		KeySourceHolder keySourceHolder = super.lower(currentKeySourceHolder);
		log.info(">>>Next to most recent KeySourceHolder in the DS:"
				+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
		return keySourceHolder;
	}

	public int getCachedKeySourceLimit() {
		return cachedKeySourceLimit;
	}

	public void printCollection() {
		Iterator<KeySourceHolder> iter = super.iterator();
		log.info(">>>Contents of the DS:");
		while (iter.hasNext()) {
			log.info("" + ((com.lithium.streams.compliance.model.KeySourceHolder) iter.next()).getTimeEpochMilli());
		}
	}

}
