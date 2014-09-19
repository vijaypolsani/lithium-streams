package com.lithium.streams.compliance.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lithium.research.config.Configs;
import lithium.research.key.KeySource;

import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.DecryptStreamer;
import com.lithium.streams.compliance.security.KeyServerDecryption;
import com.lithium.streams.compliance.security.KeyServerProperties;
import com.lithium.streams.compliance.util.KeySourceUtil;

public class MessageDecryption implements IDecryption {
	@Autowired
	private KeyServerDecryption keyServerDecryption;
	@Autowired
	private KeySourceUtil keySourceUtil;

	public KeySourceUtil getKeySourceUtil() {
		return keySourceUtil;
	}

	public void setKeySourceUtil(KeySourceUtil keySourceUtil) {
		this.keySourceUtil = keySourceUtil;
	}

	private static final Logger log = LoggerFactory.getLogger(MessageDecryption.class);

	public SecureEvent performMessageDecryption(final SecureEvent secureEvent, final String communityName,
			final KeySource source) {
		DecryptStreamer decryptStreamer = new DecryptStreamer(Configs.empty(), source);
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(secureEvent.getMessage());
		InputStream inputStream;
		try {
			log.info(">>>Performing message decryption. " + new String(secureEvent.getMessage()));
			inputStream = decryptStreamer.filterIn(byteArrayInputStream, KeyServerProperties.COMMUNITY_NAME.getValue());
			return new Payload(IOUtils.toByteArray(inputStream));
		} catch (IOException e) {
			log.error(">>>Could not decrypt the message. " + e.getLocalizedMessage());
			e.printStackTrace();
			return secureEvent;
		}
	}
}
