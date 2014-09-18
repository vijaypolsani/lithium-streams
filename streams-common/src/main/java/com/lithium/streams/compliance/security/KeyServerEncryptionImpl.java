package com.lithium.streams.compliance.security;

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

public class KeyServerEncryptionImpl implements KeyServerEncryption {
	private static final Logger log = LoggerFactory.getLogger(KeyServerEncryptionImpl.class);

	@Override
	public SecureEvent performMessageEncryption(SecureEvent event, String communityName, KeySource source) throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		EncryptStreamer encryptStreamer = new EncryptStreamer(Configs.empty(), source);
		OutputStream outputStream = encryptStreamer.filterOut(byteArrayOutputStream, KeyServerProperties.COMMUNITY_NAME
				.getValue());
		IOUtils.write(event.getMessage(), outputStream);
		IOUtils.closeQuietly(outputStream);
		log.debug("CommunityEncryptEvent completed: " + byteArrayOutputStream.toByteArray());
		return new Payload(byteArrayOutputStream.toByteArray());
	}

}
