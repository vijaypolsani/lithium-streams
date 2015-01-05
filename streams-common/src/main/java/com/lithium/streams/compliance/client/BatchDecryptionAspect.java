package com.lithium.streams.compliance.client;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static com.google.common.base.Preconditions.checkNotNull;

import com.lithium.streams.compliance.security.KeyServerEncryption;

@Aspect
public class BatchDecryptionAspect implements IDecryptionAspect {
	private static final Logger log = LoggerFactory.getLogger(BatchDecryptionAspect.class);

	@Autowired
	private KeyServerEncryption encryptEvent;

	/* (non-Javadoc)
	 * @see com.lithium.streams.compliance.util.IDecryptionAspect#decryptBeforeSending(org.aspectj.lang.ProceedingJoinPoint)
	 */
	@Override
	@Around("execution (* com.lithium.streams.compliance.security.KeyServerDecryptionImpl.performMessageDecryption(..))")
	public Object decryptBeforeSending(ProceedingJoinPoint proceedingJoinPoint) {
		checkNotNull(proceedingJoinPoint, "Joinpoint is null.");

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
