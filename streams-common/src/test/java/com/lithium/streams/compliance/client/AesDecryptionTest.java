package com.lithium.streams.compliance.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import lithium.research.key.KeySource;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lithium.streams.compliance.model.Payload;
import com.lithium.streams.compliance.model.SecureEvent;
import com.lithium.streams.compliance.security.KeyServerDecryption;
import com.lithium.streams.compliance.security.KeyServerDecryptionImpl;
import com.lithium.streams.compliance.security.KeyServerEncryption;
import com.lithium.streams.compliance.security.KeyServerEncryptionImpl;
import com.lithium.streams.compliance.security.KeyServerProperties;
import com.lithium.streams.compliance.util.FixedSizeSortedSet;
import com.lithium.streams.compliance.util.KeySourceComparator;
import com.lithium.streams.compliance.util.KeySourceUtil;

public class AesDecryptionTest {

	//private static final String HOST_KEY = "//Users/vijay.polsani/temp/host.key";
	private static final String HOST_KEY = "/home/user/host.key";

	//private static final String TEST_DATA_LOCATION = "./data/lia147.release.qa.log.xz.aes.aes";
	private static final String TEST_DATA_LOCATION = "/home/user/Downloads/data/lia147.release.qa.log.xz.aes.aes";

	private static final Logger log = LoggerFactory.getLogger(IDecryptionTest.class);

	private KeyServerEncryption encryptEvent = new KeyServerEncryptionImpl();

	private KeyServerDecryption decryptEvent = new KeyServerDecryptionImpl();

	private KeySourceUtil keySourceUtil = new KeySourceUtil(new FixedSizeSortedSet(new KeySourceComparator(), 1,
			HOST_KEY), true);

	private IDecryption iDecryption = new MessageDecryption();

	private StringBuffer sb = new StringBuffer();

	private byte[] readFully(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, length);
		}
		return baos.toByteArray();
	}

	public void before() throws FileNotFoundException {
		BasicConfigurator.resetConfiguration();
		BasicConfigurator.configure();
		//InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(TEST_DATA_LOCATION);
		File file = new File(TEST_DATA_LOCATION);
		InputStream inputStream = new FileInputStream(file);
		try {
			sb.append(readFully(inputStream));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void after() {
		sb = null;
	}

	public void checkDecryption() {
		String sampleData = sb.toString();
		log.info("Sample Text Length: " + sampleData.length());
		log.info("Sample Text Length: " + sampleData);
		//final SecureEvent inputEvent = new Payload(sampleData.getBytes(StandardCharsets.UTF_8));
		final SecureEvent eventEncrypted = new Payload(sampleData.getBytes(StandardCharsets.UTF_8));

		Optional<KeySource> source = keySourceUtil.getKeySource();
		if (source.isPresent()) {
			//SecureEvent eventEncrypted = encryptEvent.performMessageEncryption(inputEvent,KeyServerProperties.COMMUNITY_NAME.getValue(), source.get());
			if (eventEncrypted != null) {
				log.info("Encrypted Data length: " + eventEncrypted.getMessage().length);
				log.info("Encrypted Data : " + new String(eventEncrypted.getMessage()));

				SecureEvent eventDecrypted = iDecryption.performMessageDecryption(eventEncrypted,
						KeyServerProperties.COMMUNITY_NAME.getValue(), source.get());
				log.info("***Decrypted Data length: " + eventDecrypted.getMessage().length);
				log.info("***Decrypted Data: " + new String(eventDecrypted.getMessage()));

			}
		}
	}

	public static void main(String args[]) {
		//Testing on server. Need to be on 10.240.180.18 for host.key to work.
		AesDecryptionTest aesDecryptionTest = new AesDecryptionTest();
		try {
			aesDecryptionTest.before();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aesDecryptionTest.checkDecryption();
		aesDecryptionTest.after();
	}

}
