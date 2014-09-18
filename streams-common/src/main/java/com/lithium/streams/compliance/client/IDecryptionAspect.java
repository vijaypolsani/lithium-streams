package com.lithium.streams.compliance.client;

import org.aspectj.lang.ProceedingJoinPoint;

public interface IDecryptionAspect {

	public abstract Object decryptBeforeSending(ProceedingJoinPoint proceedingJoinPoint);

}