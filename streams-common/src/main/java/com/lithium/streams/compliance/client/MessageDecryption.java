package com.lithium.streams.compliance.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lithium.research.config.Configs;
import lithium.research.key.KeySource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.DecryptStreamer;
import com.lithium.streams.compliance.security.KeyServerProperties;

public class MessageDecryption implements IDecryption {


	private static final Logger log = LoggerFactory.getLogger(MessageDecryption.class);

	public SecureEvent performMessageDecryption(final SecureEvent secureEvent, final String communityName,
			final KeySource source) {
		DecryptStreamer decryptStreamer = new DecryptStreamer(Configs.empty(), source);
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(secureEvent.getMessage());
		try {
			InputStream inputStream = decryptStreamer.filterIn(byteArrayInputStream, KeyServerProperties.COMMUNITY_NAME
					.getValue());
			return new Payload(IOUtils.toByteArray(inputStream));
		} catch (StreamsCommonSecurityException | IOException e) {
			log.error(">>>Could not decrypt the message. Sending back original. " + e.getLocalizedMessage());
			return secureEvent;
		}
	}
}
