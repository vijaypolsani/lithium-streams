package com.lithium.streams.compliance.security;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import lithium.research.config.Configs;
import lithium.research.key.KeySource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;

public class KeyServerDecryptionImpl implements KeyServerDecryption {
	private static final Logger log = LoggerFactory.getLogger(KeyServerDecryptionImpl.class);

	@Override
	public SecureEvent performMessageDecryption(@Nonnull final SecureEvent secureEvent,
			@Nonnull final String communityName, @Nonnull final KeySource source) throws IOException {
		checkNotNull(secureEvent, "Message that needs ecryption must be present.");
		checkArgument(secureEvent.getMessage() == null, "Message content inside the event is empty.");
		checkNotNull(communityName, "Community Name for getting decryption key is needed.");
		checkArgument(communityName.length() == 0, "communityName name is empty: %s", communityName);
		checkNotNull(source, "Keysource utility object ceration is not complete.");

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
