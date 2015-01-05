package com.lithium.streams.compliance.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import lithium.research.key.KeySource;

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

public class EncryptionTest {
	//private final static String samplePayload = "{'formatVersion':'1.0','published':'1409160578436','generator':{'source':'meghameshram148','eventId':'478689'},'provider':{'service':'lia','version':'14.8.0'},'title':'Compliance Integration Release','verb':'EntityCreated message','encryptedPayload':'{\'actor\':{\'uid\':\'1\',\'login\':\'admin\',\'registrationStatus\':\'FULLY_REGISTERED\',\'email\':\'admin@lithium.com\',\'type\':\'user\',\'registrationTime\':\'1408569569726\'},\'target\':{\'type\':\'forum-board\',\'conversationType\':\'conversation\',\'id\':\'10\',\'conversationId\':\'4\'},\'streamObject\':{\'objectType\':\'topic\',\'id\':\'4\',\'displayName\':\'Compliance Integration Release\',\'content\':\'<p>Message with attachment</p>\',\'visibility\':\'public\',\'subject\':\'Compliance Integration Release\',\'added\':\'\',\'attachments\':[{\'id\':\'1\',\'displayFileName\':\'flash-player-properties.png\',\'base64Content\':\'iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEgAACxIB0t1+/AAAABx0RVh0U29mdHdhcmUAQWRvYmUgRmlyZXdvcmtzIENTNXG14zYAAAAWdEVYdENyZWF0aW9uIFRpbWUAMTAvMzAvMDZ8tDgbAAABx0lEQVQ4jY2TT0tbQRTFf+/5ggYKTXHhot0UitTG0G0LESxI6SaI62z7EdyI30D8HvoJVEqhu+zsSgpKazdFKC1ogkne33u6yLyXSbpx4Gxm5p57zpk7wdetLT3b22N5cxMzI8/zB6HVagUAwaelJWXA23qdBSAsIQEgwBwKBwOKLMsKIEqATKoOQiBguswnkf4ji1KJDEglgrLYdfcJBhLfzfhtxrtarSILEyAFEiD2MHaIgT8SV80m60dHfBwOSaTqfpRI5E7BpPnUe6njptnkw8kJi40Gw7MzYs9OlAIZEHuyfel9idcHByw2GnzZ3qbe62ESchlMLEjEHsbSRKZE1m7ztN0m7fd5ubtLvro6c7eyEHvdSy13ZrzodgFILy54FIbUwpCBpzBKgNyx+cWXZjze2GC92+W21+Pbzg4C7uJ48uzORpS4GYjnwnt/fMxap0M+GPBjf5+b0aiah9K/yhBzidjrLmCt0+Hv6Sk/Dw+5Pj8ndcnPD9U0A3dYEnxeWWFUFNwmCalZpcwfbZMmGRQS47kAr+7vp0WOfIbAKVhYhksFwasV6UlmZpmZpQ6Zh9ysmNlzyst/8xx4w8PXEPgFXP8DtA6DGIrzx1wAAAAASUVORK5CYII=\',\'description\':\'\',\'contentType\':\'image/png\',\'lastModified\':\'\'}],\'postTime\':\'1409160578198\',\'isTopic\':\'false\'}}'}";
	private final static String samplePayload = "**TEST MESSAGE**";
	private final static SecureEvent inputEvent = new Payload(samplePayload.getBytes(StandardCharsets.UTF_8));

	private static final Logger log = LoggerFactory.getLogger(EncryptionTest.class);

	private KeyServerEncryption encryptEvent = new KeyServerEncryptionImpl();

	private KeyServerDecryption decryptEvent = new KeyServerDecryptionImpl();

	private KeySourceUtil keySourceUtil = new KeySourceUtil(new FixedSizeSortedSet(new KeySourceComparator(), 1, "/home/user/host.key"), true);

	public static void main(String args[]) {
		new EncryptionTest().initialize();
	}

	public void initialize() {
		//KeyServerEncryption encryptEvent = (KeyServerEncryption) context.getBean("keyServerEncryption");
		//KeyServerDecryption decryptEvent = (KeyServerDecryption) context.getBean("keyServerDecryption");
		//KeySourceUtil keySourceUtil = (KeySourceUtil) context.getBean("keySourceUtil");
		try {
			log.info("Sample Text Length: " + samplePayload.length());
			log.info("Sample Text Length: " + samplePayload);

			Optional<KeySource> source = keySourceUtil.getKeySource();
			if (source.isPresent()) {
				SecureEvent eventEncrypted = encryptEvent.performMessageEncryption(inputEvent,
						KeyServerProperties.COMMUNITY_NAME.getValue(), source.get());
				if (eventEncrypted != null) {
					log.info("Encrypted Data length: " + eventEncrypted.getMessage().length);
					log.info("Encrypted Data : " + new String(eventEncrypted.getMessage()));

					SecureEvent eventDecrypted = decryptEvent.performMessageDecryption(eventEncrypted,
							KeyServerProperties.COMMUNITY_NAME.getValue(), source.get());
					log.info("***Decrypted Data length: " + eventDecrypted.getMessage().length);
					log.info("***Decrypted Data: " + new String(eventDecrypted.getMessage()));

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
