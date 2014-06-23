package com.lithium.streams.compliance.beans;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lithium.streams.compliance.consumer.ConsumeMessages;
import com.lithium.streams.compliance.consumer.ConsumerGroupRemovalListener;

public class StreamCache {

	private Cache<String, ConsumeMessages> cache = null;

	public StreamCache(ConsumerGroupRemovalListener listener) {
		cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(7, TimeUnit.DAYS).removalListener(
				listener).build();
	}

	/**
	 * @return the cache
	 */
	public Cache<String, ConsumeMessages> getCache() {
		return cache;
	}

	@Override
	public String toString() {
		return "StreamCache [cache=" + cache.asMap().keySet().toArray().toString() + "]";
	}

}
