package com.lithium.streams.compliance.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lithium.research.config.Configs;
import lithium.research.key.KeySource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.EncryptStreamer;
import com.lithium.streams.compliance.security.KeyServerProperties;

public class MessageEncryption implements IEncryption {

	private static final Logger log = LoggerFactory.getLogger(MessageEncryption.class);

	@Override
	public SecureEvent performMessageEncryption(final SecureEvent secureEvent, final String communityName,
			final KeySource source) {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			EncryptStreamer encryptStreamer = new EncryptStreamer(Configs.empty(), source);
			OutputStream outputStream = encryptStreamer.filterOut(byteArrayOutputStream,
					KeyServerProperties.COMMUNITY_NAME.getValue());
			IOUtils.write(secureEvent.getMessage(), outputStream);
			IOUtils.closeQuietly(outputStream);
			return new Payload(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			log.error("<<< Encryption failed : " + e.getLocalizedMessage());
			return secureEvent;
		}
	}
}
