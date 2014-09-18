package com.lithium.streams.compliance.client;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class RealTimeEncryptionAspect implements IEncryptionAspect {
	private static final Logger log = LoggerFactory.getLogger(RealTimeEncryptionAspect.class);

	@Around("execution (* com.lithium.streams.compliance.security.KeyServerEncryptionImpl.performMessageEncryption(..))")
	@Override
	public Object encryptBeforeSending(ProceedingJoinPoint proceedingJoinPoint) {
		log.info(">>>EncryptBeforeSending() Aspect is running! method Name: "
				+ proceedingJoinPoint.getSignature().getName());
		Object[] args = proceedingJoinPoint.getArgs();
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				log.debug(">>> Arg: " + (i + 1) + ":" + args[i]);
			}
		}
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log.info(">>>Completed encryption and returned: " + result);
		return result;
	}
}
