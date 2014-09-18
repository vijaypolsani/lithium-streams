package com.lithium.streams.compliance.util;

import static com.google.common.io.BaseEncoding.base16;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import lithium.research.key.KeySource;
import lithium.research.keys.ClientKeySource;

import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.security.KeyServerProperties;

public class KeySourceUtil {
	private final FixedSizeSortedSet<KeySource> fixedSizeSortedSet;

	public KeySourceUtil(FixedSizeSortedSet<KeySource> fixedSizeSortedSet) {
		this.fixedSizeSortedSet = fixedSizeSortedSet;
	}

	public Optional<KeySource> getKeySource() throws InterruptedException {
		KeySource source = null;
		if (source == null) {
			try {
				source = new ClientKeySource(URI.create(KeyServerProperties.URI_LINK.getValue()),
						KeyServerProperties.EMAIL.getValue(), new SecretKeySpec(base16().decode(
								KeyServerProperties.USER_KEY.getValue()), KeyServerProperties.AES.getValue()),
						getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
								+ KeyServerProperties.HOST_KEY_PATH.getValue());
				fixedSizeSortedSet.add(source);
			} catch (IOException e) {
				e.printStackTrace();
				throw new StreamsCommonSecurityException("STR-COMM-0001",
						"Exception in creation of Keys from KeyServer.", e);
			}
		}
		return Optional.of(source);
	}

}
