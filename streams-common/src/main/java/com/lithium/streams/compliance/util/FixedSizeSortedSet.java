package com.lithium.streams.compliance.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeSet;

import lithium.research.key.KeySource;

import com.lithium.streams.compliance.model.KeySourceHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedSizeSortedSet<KeySourceHolder> extends TreeSet<KeySourceHolder> {

	private static final long serialVersionUID = 4133039913231315811L;
	private final Comparator<KeySourceHolder> comparator;
	private final int maxSize;
	private static final Logger log = LoggerFactory.getLogger(FixedSizeSortedSet.class);

	public FixedSizeSortedSet(Comparator<KeySourceHolder> comparator, int maxSize) {
		super(Collections.synchronizedSortedSet(new TreeSet<KeySourceHolder>(comparator)));
		this.comparator = comparator;
		this.maxSize = maxSize;
	}

	@Override
	public boolean add(KeySourceHolder keySourceHolder) {
		if (size() >= maxSize) {
			KeySourceHolder smallest = first();
			int comparision = comparator.compare(keySourceHolder, smallest);
			if (comparision > 0) {
				remove(smallest);
				log.debug(">>> Older KeySource timestamp removed: "
						+ ((com.lithium.streams.compliance.model.KeySourceHolder) smallest).getTimeEpochMilli());
				log.debug(">>> Newly Added KeySource timestamp added: "
						+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
				return super.add(keySourceHolder);
			}
			return false;
		} else {
			log.debug(">>> New Key found & added: "
					+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
			return super.add(keySourceHolder);
		}
	}

	public void printCollection() {
		Iterator<KeySourceHolder> iter = super.iterator();
		log.debug(">>>Contents of the DS:");
		while (iter.hasNext()) {
			log.debug("" + ((com.lithium.streams.compliance.model.KeySourceHolder) iter.next()).getTimeEpochMilli());
		}
	}

	public Optional<KeySource> getKeySource() {
		KeySourceHolder keySourceHolder = super.last();
		log.debug(">>>Most recent KeySource in the DS: "
				+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
		return ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getKeysource();
	}

	public Optional<KeySource> getKeySource(KeySourceHolder currentKeySourceHolder) {
		KeySourceHolder keySourceHolder = super.lower(currentKeySourceHolder);
		if (keySourceHolder == null)
			return Optional.empty();
		log.debug(">>>Next to most recent KeySource in the DS:"
				+ ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getTimeEpochMilli());
		return ((com.lithium.streams.compliance.model.KeySourceHolder) keySourceHolder).getKeysource();
	}
}
