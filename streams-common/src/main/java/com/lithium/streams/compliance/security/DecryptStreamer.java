package com.lithium.streams.compliance.security;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;

import lithium.research.config.Config;
import lithium.research.key.KeySource;
import lithium.research.replacer.NoOpStringReplacer;
import lithium.research.replacer.RegexStringReplacer;
import lithium.research.replacer.StringReplacer;

/**
 * Decrypt Utility
 *
 */
public class DecryptStreamer {
	private final KeySource keySource;
	private final byte[] header;
	private final String cipherName;
	private final StringReplacer replacer;

	public DecryptStreamer(@Nonnull Config config, @Nonnull KeySource keySource) {
		checkNotNull(config);
		checkNotNull(keySource);

		header = config.getString("crypt.header", "LiAESv01").getBytes();
		System.out.println("Header: " + new String(header));
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
	public InputStream filterIn(@Nonnull InputStream in, @Nullable String name) throws IOException {
		checkArgument(name != null, "name must be specified for key lookup");
		assert name != null;
		try {
			for (Key key : keySource.getKeys(replacer.replace(name))) {
				System.out.println("Name " + name + " Key " + new String(key.getEncoded()));
				in = decryptIn(in, key);
			}
		} catch (GeneralSecurityException e) {
			throw new IOException("decrypt failed", e);
		}
		return in;
	}

	@Nonnull
	private InputStream decryptIn(@Nonnull InputStream in, @Nonnull Key key) throws IOException,
			GeneralSecurityException {

		byte[] inHeader = new byte[header.length];
		int read = in.read(inHeader);
		System.out.println("Read length " + read);
		if (read != inHeader.length) {
			throw new IOException("read didn't complete for header");
		}
		if (!Arrays.equals(header, inHeader)) {
			throw new IOException("unexpected header: '" + new String(inHeader) + "', expected: '" + new String(header)
					+ "'");
		}
		
		Cipher cipher = Cipher.getInstance(cipherName);
		byte[] iv = new byte[cipher.getBlockSize()];
		if (in.read(iv) != iv.length) {
			throw new IOException("read didn't complete for iv");
		}
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);

		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		return new CipherInputStream(in, cipher);
	}

}
