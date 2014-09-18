package com.lithium.streams.compliance.client;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import lithium.research.key.KeySource;

import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.KeyServerDecryption;
import com.lithium.streams.compliance.security.KeyServerProperties;
import com.lithium.streams.compliance.util.KeySourceUtil;

public class MessageDecryption implements IDecryption {
	@Autowired
	private KeyServerDecryption keyServerDecryption;
	@Autowired
	private KeySourceUtil keySourceUtil;

	private static final Logger log = LoggerFactory.getLogger(MessageDecryption.class);

	public synchronized byte[] performMessageDecryption(final SecureEvent secureEvent) {
		SecureEvent eventDecrypted = null;
		Optional<KeySource> source = null;
		try {
			try {
				source = keySourceUtil.getKeySource();
			} catch (InterruptedException e) {
				log.error("Exception in getting KeySource Object.");
				e.printStackTrace();
			}
			eventDecrypted = keyServerDecryption.performMessageDecryption(secureEvent,
					KeyServerProperties.COMMUNITY_NAME.getValue(), source.get());

		} catch (StreamsCommonSecurityException scse) {
			log.error("Creation of KeySource & getting Keys failed. Please check configuration."
					+ scse.getLocalizedMessage());
			scse.printStackTrace();
		} catch (IOException e) {
			log.error("Event Decryption failed. Please check the server access." + e.getLocalizedMessage());
			e.printStackTrace();
		}
		if (eventDecrypted != null) {
			log.info("***Decrypted Data length: " + eventDecrypted.getMessage().length);
			log.info("***Decrypted Data: " + new String(eventDecrypted.getMessage()));
			return eventDecrypted.getMessage();
		} else
			return secureEvent.getMessage();

	}

}
