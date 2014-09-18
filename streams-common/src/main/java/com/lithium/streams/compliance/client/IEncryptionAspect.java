package com.lithium.streams.compliance.client;

import org.aspectj.lang.ProceedingJoinPoint;

public interface IEncryptionAspect {
	public Object encryptBeforeSending(ProceedingJoinPoint proceedingJoinPoint);
}