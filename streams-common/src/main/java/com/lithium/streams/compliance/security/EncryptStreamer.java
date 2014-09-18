package com.lithium.streams.compliance.security;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.List;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

import lithium.research.config.Config;
import lithium.research.key.KeySource;
import lithium.research.replacer.NoOpStringReplacer;
import lithium.research.replacer.RegexStringReplacer;
import lithium.research.replacer.StringReplacer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.HostKeyMachineMisMatchSecurityException;

/**
 * Utility to Encrypt
 *
 */
public class EncryptStreamer {
	private final KeySource keySource;
	private final byte[] header;
	private final String cipherName;
	private final StringReplacer replacer;
	private static final Logger log = LoggerFactory.getLogger(EncryptStreamer.class);

	public EncryptStreamer(@Nonnull Config config, @Nonnull KeySource keySource) {
		checkNotNull(config);
		checkNotNull(keySource);
		header = config.getString("crypt.header", "LiAESv01").getBytes();
		cipherName = config.getString("crypt.cipher", "AES/CBC/PKCS5Padding");
		if (config.containsKey("crypt.regex")) {
			replacer = new RegexStringReplacer(config.getString("crypt.regex"), config.getString("crypt.replacement",
					"$1"));
		} else {
			replacer = new NoOpStringReplacer();
		}
		this.keySource = keySource;
	}

	@Nonnull
	public OutputStream filterOut(@Nonnull OutputStream out, String name) throws IOException {
		List<Key> keys = null;
		try {
			keys = keySource.getKeys(replacer.replace(name));
		} catch (RuntimeException re) {
			log.error("Exception in getting keys from KeyServer. Check host.key mismatch!" + re.getLocalizedMessage());
			throw new HostKeyMachineMisMatchSecurityException("CSE0002",
					"HostKey and IP/Machine mismatch. Check key validity.", re);
		}

		try {
			for (Key key : keys) {
				System.out.println("Name " + name + " Key " + new String(key.getEncoded()));
				out = encryptOut(out, key);
			}
		} catch (GeneralSecurityException e) {
			throw new IOException("encrypt failed", e);
		}
		return out;
	}

	@Nonnull
	private OutputStream encryptOut(@Nonnull OutputStream out, @Nonnull Key key) throws IOException,
			GeneralSecurityException {
		out.write(header);
		Cipher cipher = Cipher.getInstance(cipherName);
		byte[] iv = new byte[cipher.getBlockSize()];
		new SecureRandom().nextBytes(iv);
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);
		out.write(iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		System.out.println("Encrypted stream ");
		return new CipherOutputStream(out, cipher);
	}
}
