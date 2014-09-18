package com.lithium.streams.compliance.util;

import java.util.Comparator;

import com.lithium.streams.compliance.model.KeySourceHolder;

public class KeySourceComparator implements Comparator<KeySourceHolder> {

	@Override
	public int compare(KeySourceHolder keySourceHolderOlder, KeySourceHolder keySourceHolderNewer) {
		if (keySourceHolderOlder.getTimeEpochMilli() > keySourceHolderNewer.getTimeEpochMilli())
			return 1;
		else if (keySourceHolderOlder.getTimeEpochMilli() < keySourceHolderNewer.getTimeEpochMilli())
			return -1;
		else
			return 0;
	}

}
