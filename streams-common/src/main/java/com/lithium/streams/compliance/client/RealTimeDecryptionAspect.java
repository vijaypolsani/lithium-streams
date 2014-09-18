package com.lithium.streams.compliance.client;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lithium.streams.compliance.security.KeyServerEncryption;

@Aspect
public class RealTimeDecryptionAspect implements IDecryptionAspect {
	private static final Logger log = LoggerFactory.getLogger(RealTimeDecryptionAspect.class);

	@Autowired
	private KeyServerEncryption encryptEvent;

	@Override
	@Around("execution (* com.lithium.streams.compliance.consumer.ConsumerCallable.performMessageDecryption(..))")
	public Object decryptBeforeSending(ProceedingJoinPoint proceedingJoinPoint) {
		log.info(">>>DecryptBeforeSending() Aspect is running! method Name: "
				+ proceedingJoinPoint.getSignature().getName());
		Object[] args = proceedingJoinPoint.getArgs();
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				log.debug(">>>Arg: " + (i + 1) + ":" + args[i]);
			}
		}
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		log.info(">>>Completed decryption and returned: " + result);
		return result;
	}
}
