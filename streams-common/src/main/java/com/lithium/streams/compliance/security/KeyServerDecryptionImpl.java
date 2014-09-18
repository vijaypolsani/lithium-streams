package com.lithium.streams.compliance.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lithium.research.config.Configs;
import lithium.research.key.KeySource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.client.RealTimeEncryptionAspect;
import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;

public class KeyServerDecryptionImpl implements KeyServerDecryption {
	private static final Logger log = LoggerFactory.getLogger(KeyServerDecryptionImpl.class);

	@Override
	public SecureEvent performMessageDecryption(SecureEvent event, String communityName, KeySource source) throws IOException {
		DecryptStreamer decryptStreamer = new DecryptStreamer(Configs.empty(), source);
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(event.getMessage());
		InputStream inputStream = decryptStreamer.filterIn(byteArrayInputStream, KeyServerProperties.COMMUNITY_NAME
				.getValue());
		return new Payload(IOUtils.toByteArray(inputStream));
	}
}
