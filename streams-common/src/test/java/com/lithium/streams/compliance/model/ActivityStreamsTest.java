package com.lithium.streams.compliance.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ActivityStreamsTest {
	private static final ActivityStreamEncrypted activityStreamEncrypted = new ActivityStreamEncrypted();
	private static final ActivityStream activityStream = new ActivityStream();
	private static final ActivityStream.Actor actor = new ActivityStream.Actor();

	@BeforeClass
	public static void prepareTesting() {
		activityStreamEncrypted.setPublished("1409160578436");
		activityStreamEncrypted.setTitle("Compliance Integration Release");
		activityStreamEncrypted.setVerb("EntityCreated message");
		activityStreamEncrypted
				.setEncryptedPayload("{\"actor\":{\"uid\":\"1\",\"login\":\"admin\",\"registrationStatus\":\"FULLY_REGISTERED\",\"email\":\"admin@lithium.com\",\"type\":\"user\",\"registrationTime\":\"1408569569726\"},\"target\":{\"type\":\"forum-board\",\"conversationType\":\"conversation\",\"id\":\"10\",\"conversationId\":\"4\"},\"streamObject\":{\"objectType\":\"topic\",\"id\":\"4\",\"displayName\":\"Compliance Integration Release\",\"content\":\"<p>Message with attachment</p>\",\"visibility\":\"public\",\"subject\":\"Compliance Integration Release\",\"added\":\"\",\"attachments\":[{\"id\":\"1\",\"displayFileName\":\"flash-player-properties.png\",\"base64Content\":\"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEgAACxIB0t1+/AAAABx0RVh0U29mdHdhcmUAQWRvYmUgRmlyZXdvcmtzIENTNXG14zYAAAAWdEVYdENyZWF0aW9uIFRpbWUAMTAvMzAvMDZ8tDgbAAABx0lEQVQ4jY2TT0tbQRTFf+/5ggYKTXHhot0UitTG0G0LESxI6SaI62z7EdyI30D8HvoJVEqhu+zsSgpKazdFKC1ogkne33u6yLyXSbpx4Gxm5p57zpk7wdetLT3b22N5cxMzI8/zB6HVagUAwaelJWXA23qdBSAsIQEgwBwKBwOKLMsKIEqATKoOQiBguswnkf4ji1KJDEglgrLYdfcJBhLfzfhtxrtarSILEyAFEiD2MHaIgT8SV80m60dHfBwOSaTqfpRI5E7BpPnUe6njptnkw8kJi40Gw7MzYs9OlAIZEHuyfel9idcHByw2GnzZ3qbe62ESchlMLEjEHsbSRKZE1m7ztN0m7fd5ubtLvro6c7eyEHvdSy13ZrzodgFILy54FIbUwpCBpzBKgNyx+cWXZjze2GC92+W21+Pbzg4C7uJ48uzORpS4GYjnwnt/fMxap0M+GPBjf5+b0aiah9K/yhBzidjrLmCt0+Hv6Sk/Dw+5Pj8ndcnPD9U0A3dYEnxeWWFUFNwmCalZpcwfbZMmGRQS47kAr+7vp0WOfIbAKVhYhksFwasV6UlmZpmZpQ6Zh9ysmNlzyst/8xx4w8PXEPgFXP8DtA6DGIrzx1wAAAAASUVORK5CYII=\",\"description\":\"\",\"contentType\":\"image/png\",\"lastModified\":\"\"}],\"postTime\":\"1409160578198\",\"isTopic\":\"false\"}}");
	}

	@Before
	public void beforeStartTest() {

	}

	@After
	public void cleanupAfterTest() {
	}

	@AfterClass
	public static void cleanUp() {

	}

	@Test
	public void ActivityStreamsEncryption() {

	}

}
