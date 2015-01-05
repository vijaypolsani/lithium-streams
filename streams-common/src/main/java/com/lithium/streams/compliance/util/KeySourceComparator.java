package com.lithium.streams.compliance.util;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;
import com.lithium.streams.compliance.model.KeySourceHolder;

public class KeySourceComparator implements Comparator<KeySourceHolder> {

	@Override
	public int compare(KeySourceHolder keySourceHolderOlder, KeySourceHolder keySourceHolderNewer) {
		return ComparisonChain.start().compare(keySourceHolderOlder.getTimeEpochMilli(),
				keySourceHolderNewer.getTimeEpochMilli()).result();
	}
}
