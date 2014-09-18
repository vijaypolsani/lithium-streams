package com.lithium.streams.compliance.model;

import java.util.ArrayList;
import java.util.List;

public class ActivityStream {

	private final String formatVersion = "1.0";
	private String published = "";
	private String title = "";
	private String verb = "";

	private final Generator generator = new Generator();
	private final Provider provider = new Provider();
	private final Actor actor = new Actor();
	private final StreamObject streamObject = new StreamObject();
	private final Target target = new Target();

	public String getFormatVersion() {
		return formatVersion;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
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

	public Generator getGenerator() {
		return generator;
	}

	public Provider getProvider() {
		return provider;
	}

	public Actor getActor() {
		return actor;
	}

	public StreamObject getStreamObject() {
		return streamObject;
	}

	public Target getTarget() {
		return target;
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

	public static final class Actor {
		private String uid = "";
		private String login = "";
		private String registrationStatus = "";
		private String email = "";
		private String type = "";
		private String registrationTime = "";

		public String getRegistrationTime() {
			return registrationTime;
		}

		public void setRegistrationTime(String registrationTime) {
			this.registrationTime = registrationTime;
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getRegistrationStatus() {
			return registrationStatus;
		}

		public void setRegistrationStatus(String registrationStatus) {
			this.registrationStatus = registrationStatus;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	public static final class Attachment {
		private String id = "";
		private String displayFileName = "";
		private String base64Content = "";
		private String description = "";
		private String contentType = "";
		private String lastModified = "";

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDisplayFileName() {
			return displayFileName;
		}

		public void setDisplayFileName(String displayFileName) {
			this.displayFileName = displayFileName;
		}

		public String getBase64Content() {
			return base64Content;
		}

		public void setBase64Content(String base64Content) {
			this.base64Content = base64Content;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getLastModified() {
			return lastModified;
		}

		public void setLastModified(String lastModified) {
			this.lastModified = lastModified;
		}
	}

	public static final class StreamObject {
		private String objectType = "";
		private String id = "";
		private String displayName = "";
		private String content = "";
		private String visibility = "";
		private String subject = "";
		private String added = "";
		private String postTime = "";
		private String isTopic = "false";
		private final List<Attachment> attachments = new ArrayList<>();

		public List<Attachment> getAttachments() {
			return attachments;
		}

		public void addAttachments(Attachment attachment) {
			this.attachments.add(attachment);
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getPostTime() {
			return postTime;
		}

		public void setPostTime(String postTime) {
			this.postTime = postTime;
		}

		public String getIsTopic() {
			return isTopic;
		}

		public void setIsTopic(String isTopic) {
			this.isTopic = isTopic;
		}

		public String getVisibility() {
			return visibility;
		}

		public void setVisibility(String visibility) {
			this.visibility = visibility;
		}

		public String getObjectType() {
			return objectType;
		}

		public void setObjectType(String objectType) {
			this.objectType = objectType;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getAdded() {
			return added;
		}

		public void setAdded(String added) {
			this.added = added;
		}
	}

	public static final class Target {
		private String type = "";
		private String conversationType = "";
		private String id = "";
		private String conversationId = "";

		public String getConversationId() {
			return conversationId;
		}

		public void setConversationId(String conversationId) {
			this.conversationId = conversationId;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getConversationType() {
			return conversationType;
		}

		public void setConversationType(String conversationtype) {
			this.conversationType = conversationtype;
		}
	}
}