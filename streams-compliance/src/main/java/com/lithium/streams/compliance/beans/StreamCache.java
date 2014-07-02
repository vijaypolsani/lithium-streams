package com.lithium.streams.compliance.beans;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lithium.streams.compliance.consumer.ConsumeMessages;
import com.lithium.streams.compliance.consumer.ConsumerGroupRemovalListener;

@Path("cache")
@Component
public class StreamCache {

	private Cache<String, ConsumeMessages> cache = null;

	public StreamCache(ConsumerGroupRemovalListener listener) {
		cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.DAYS).removalListener(
				listener).build();
	}

	public StreamCache() {
		cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(30, TimeUnit.DAYS).build();
	}

	public Cache<String, ConsumeMessages> getCache() {
		return cache;
	}

	@Override
	public String toString() {
		return "StreamCache [cache=" + cache.asMap().keySet().toArray().toString() + "]";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getStats() {
		return cache.stats().toString();
	}

}
