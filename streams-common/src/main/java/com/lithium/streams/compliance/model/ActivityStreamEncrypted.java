package com.lithium.streams.compliance.model;

public class ActivityStreamEncrypted {

	private final String formatVersion = "1.0";
	private String published = "";
	private String title = "";
	private String verb = "";
	private String encryptedPayload = "";

	private final Generator generator = new Generator();
	private final Provider provider = new Provider();

	public String getFormatVersion() {
		return formatVersion;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public Generator getGenerator() {
		return generator;
	}

	public Provider getProvider() {
		return provider;
	}

	public String getEncryptedPayload() {
		return encryptedPayload;
	}

	public void setEncryptedPayload(String encryptedPayload) {
		this.encryptedPayload = encryptedPayload;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public static final class Generator {

		private String source = "";
		private String eventId = "";

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}
	}

	public static final class Provider {
		private String service = "";
		private String version = "";

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

	}
}