package com.lithium.streams.compliance.security;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import lithium.research.config.Config;
import lithium.research.key.KeySource;
import lithium.research.replacer.NoOpStringReplacer;
import lithium.research.replacer.RegexStringReplacer;
import lithium.research.replacer.StringReplacer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.exception.CipherReadSecurityException;
import com.lithium.streams.compliance.exception.StreamsCommonSecurityException;
import com.lithium.streams.compliance.exception.UnexpectedHeaderSecurityException;

/**
 * Decrypt Utility
 *
 */
public class DecryptStreamer {
	private final KeySource keySource;
	private final byte[] header;
	private final String cipherName;
	private final StringReplacer replacer;
	private static final Logger log = LoggerFactory.getLogger(DecryptStreamer.class);

	public DecryptStreamer(@Nonnull Config config, @Nonnull KeySource keySource) {
		checkNotNull(config);
		checkNotNull(keySource);

		header = config.getString("crypt.header", "LiAESv01").getBytes();
		log.info("Header: " + new String(header));
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
	public InputStream filterIn(@Nonnull InputStream in, @Nullable String name) throws StreamsCommonSecurityException {
		checkArgument(name != null, "name must be specified for key lookup");
		assert name != null;
		try {
			for (Key key : keySource.getKeys(replacer.replace(name))) {
				System.out.println("Name " + name + " Key " + new String(key.getEncoded()));
				in = decryptIn(in, key);
			}
		} catch (UnexpectedHeaderSecurityException use) {
			throw new StreamsCommonSecurityException("Decryption Failed. Header Mismatch. ", use);
		} catch (CipherReadSecurityException ce) {
			throw new StreamsCommonSecurityException("Decryption Failed. Cipher Wrong. ", ce);
		} catch (IOException io) {
			throw new StreamsCommonSecurityException("Decryption Failed. IO Failed. ", io);
		}
		return in;
	}

	@Nonnull
	private InputStream decryptIn(@Nonnull InputStream in, @Nonnull Key key) throws IOException,
			UnexpectedHeaderSecurityException, CipherReadSecurityException {

		byte[] inHeader = new byte[header.length];
		int read = in.read(inHeader);
		log.info("Read length " + read);
		if (read != inHeader.length) {
			throw new IOException("read didn't complete for header");
		}
		if (!Arrays.equals(header, inHeader)) {
			throw new UnexpectedHeaderSecurityException("unexpected header: '" + new String(inHeader)
					+ "', expected: '" + new String(header) + "'");
		}

		Cipher cipher;
		try {
			cipher = Cipher.getInstance(cipherName);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CipherReadSecurityException("read didn't complete for iv", e);
		}
		byte[] iv = new byte[cipher.getBlockSize()];
		if (in.read(iv) != iv.length) {
			throw new CipherReadSecurityException("read didn't complete for iv");
		}
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);

		try {
			cipher.init(Cipher.DECRYPT_MODE, key, spec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new CipherReadSecurityException("read didn't complete for iv", e);
		}
		return new CipherInputStream(in, cipher);
	}

}
