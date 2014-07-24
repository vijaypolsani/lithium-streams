package com.lithium.streams.compliance.consumer;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class GenericObjectPoolConfigWrapper {

	// internal vars ----------------------------------------------------------

	private final GenericObjectPoolConfig config;

	// constructors -----------------------------------------------------------

	public GenericObjectPoolConfigWrapper() {
		this.config = new GenericObjectPoolConfig();
	}

	// getters & setters ------------------------------------------------------

	public GenericObjectPoolConfig getConfig() {
		return config;
	}

	public int getMaxIdle() {
		return this.config.getMaxIdle();
	}

	public void setMaxIdle(int maxIdle) {
		this.config.setMaxIdle(maxIdle);
	}

	public int getMinIdle() {
		return this.config.getMinIdle();
	}

	public void setMinIdle(int minIdle) {
		this.config.setMinIdle(minIdle);
	}

	public int getMaxTotal() {
		return this.config.getMaxTotal();
	}

	public void setMaxTotal(int maxTotal) {
		this.config.setMaxTotal(maxTotal);
	}

	public long getMaxWait() {
		return this.config.getMaxWaitMillis();
	}

	public void setMaxWait(long maxWaitMillis) {
		this.config.setMaxWaitMillis(maxWaitMillis);
	}

	public boolean getWhenExhaustedAction() {
		return this.config.getBlockWhenExhausted();
	}

	public void setWhenExhaustedAction(boolean blockWhenExhausted) {
		this.config.setBlockWhenExhausted(blockWhenExhausted);
	}

	public boolean isTestOnBorrow() {
		return this.config.getTestOnBorrow();
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.config.setTestOnBorrow(testOnBorrow);
	}

	public boolean isTestOnReturn() {
		return this.config.getTestOnReturn();
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.config.setTestOnReturn(testOnReturn);
	}

	public boolean isTestWhileIdle() {
		return this.config.getTestWhileIdle();
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.config.setTestWhileIdle(testWhileIdle);
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return this.config.getTimeBetweenEvictionRunsMillis();
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	}

	public int getNumTestsPerEvictionRun() {
		return this.config.getNumTestsPerEvictionRun();
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
	}

	public long getMinEvictableIdleTimeMillis() {
		return this.config.getMinEvictableIdleTimeMillis();
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	}

	public long getSoftMinEvictableIdleTimeMillis() {
		return this.config.getSoftMinEvictableIdleTimeMillis();
	}

	public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
		this.config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
	}

	public boolean isLifo() {
		return this.config.getLifo();
	}

	public void setLifo(boolean lifo) {
		this.config.setLifo(lifo);
	}
}