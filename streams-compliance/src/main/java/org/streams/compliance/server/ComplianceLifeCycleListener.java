package org.streams.compliance.server;

import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComplianceLifeCycleListener extends AbstractLifeCycle.AbstractLifeCycleListener {
	private static final Logger log = LoggerFactory.getLogger(ComplianceLifeCycleListener.class);

	public ComplianceLifeCycleListener() {
	}

	@Override
	public void lifeCycleStarted(LifeCycle event) {
		log.info("lifeCycleStarted :" + event.toString());
	}

	@Override
	public void lifeCycleStarting(LifeCycle event) {
		log.info("lifeCycleStarting :" + event.toString());
	}

	@Override
	public void lifeCycleStopped(LifeCycle event) {
		log.info("lifeCycleStopped :" + event.toString());
	}

	@Override
	public void lifeCycleStopping(LifeCycle event) {
		log.info("lifeCycleStopping :" + event.toString());
	}

	@Override
	public void lifeCycleFailure(LifeCycle event, Throwable cause) {
		log.info("lifeCycleFailure :" + event.toString());
	}
}
