package com.lithium.streams.compliance.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base16;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import lithium.research.key.KeySource;
import lithium.research.keys.ClientKeySource;

import org.apache.commons.net.nntp.Threadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.KeyServerFetchSecurityException;
import com.lithium.streams.compliance.model.KeySourceHolder;

public class KeySourceUtil {
	private final FixedSizeSortedSet<KeySourceHolder> fixedSizeSortedSet;
	private final Boolean turonOnEncryption;
	private static final Logger log = LoggerFactory.getLogger(KeySourceUtil.class);

	public KeySourceUtil(FixedSizeSortedSet<KeySourceHolder> fixedSizeSortedSet, boolean turonOnEncryption) {
		this.fixedSizeSortedSet = fixedSizeSortedSet;
		this.turonOnEncryption = turonOnEncryption;
		initializeKeySourceStash();
	}

	private void initializeKeySourceStash() {
		log.info(">>>Initializing the KeySource Store: Limit = " + fixedSizeSortedSet.getCachedKeySourceLimit());
		for (int i = fixedSizeSortedSet.getCachedKeySourceLimit(); i > 0; i--) {
			add(new KeySourceHolder(fixedSizeSortedSet.createKeySource()));
		}
		fixedSizeSortedSet.printCollection();
	}

	public void refreshKeySourceStash() {
		initializeKeySourceStash();
	}

	public boolean add(KeySourceHolder keySourceHolder) {
		checkNotNull(keySourceHolder, "Null KeySource cannot be added.");
		return fixedSizeSortedSet.add(keySourceHolder);
	}

	public Optional<KeySource> getKeySource() {
		if (!turonOnEncryption.booleanValue()) {
			log.info("Encryption is tuned OFF. So no keySource in the store.");
			return Optional.empty();
		}
		return fixedSizeSortedSet.getKeySource();
	}

	public KeySourceHolder getKeySourceHolder() {
		return fixedSizeSortedSet.getKeySourceHolder();
	}

	public Optional<KeySource> getOlderKeySource(KeySourceHolder currentKeySourceHolder) {
		checkNotNull(currentKeySourceHolder, "Null Current KeySource cannot be used to check for older KeySource.");
		return fixedSizeSortedSet.getKeySource(currentKeySourceHolder);
	}

	public KeySourceHolder getOlderKeySourceHolder(KeySourceHolder currentKeySourceHolder) {
		checkNotNull(currentKeySourceHolder, "Null Current KeySource cannot be used to check for older KeySource.");
		return fixedSizeSortedSet.getOlderKeySourceHolder(currentKeySourceHolder);
	}

	public void listKeyStore() {
		fixedSizeSortedSet.printCollection();
	}
}
