package com.lithium.streams.compliance.security;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;

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
	public SecureEvent performMessageEncryption(@Nonnull final SecureEvent secureEvent,
			@Nonnull final String communityName, @Nonnull final KeySource source) throws IOException {
		checkNotNull(secureEvent, "Message that needs ecryption must be present.");
		checkArgument(secureEvent.getMessage() == null, "Message content inside the event is empty.");
		checkNotNull(communityName, "Community Name for getting decryption key is needed.");
		checkArgument(communityName.length() == 0, "communityName name is empty: %s", communityName);
		checkNotNull(source, "Keysource utility object ceration is not complete.");
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
