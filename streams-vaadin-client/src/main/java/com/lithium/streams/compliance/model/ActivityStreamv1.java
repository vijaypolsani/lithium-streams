package com.lithium.streams.compliance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityStreamv1 {
	private final String formatVersion = "1.0";

	@JsonProperty("event")
	private final Event event = new Event();

	@JsonProperty("payload")
	private final Payload payload = new Payload();

	private final Generator generator = new Generator();
	private final Provider provider = new Provider();
	private final Actor actor = new Actor();
	private final StreamObject streamObject = new StreamObject();
	private final Target target = new Target();

	public String getFormatVersion() {
		return formatVersion;
	}

	public Event getEvent() {
		return event;
	}

	public Payload getPayload() {
		return payload;
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

	public class Event {
		private String id = "";
		//type
		@JsonProperty("type")
		private String verb = "";
		//time
		@JsonProperty("time")
		private String published = "";
		private String frameId = "";
		private String version = "";
		private String service = "";
		private String source = "";
		private String stats = "";
		private String node = "";

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getVerb() {
			return verb;
		}

		public void setVerb(String verb) {
			this.verb = verb;
		}

		public String getPublished() {
			return published;
		}

		public void setPublished(String published) {
			this.published = published;
		}

		public String getFrameId() {
			return frameId;
		}

		public void setFrameId(String frameId) {
			this.frameId = frameId;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getStats() {
			return stats;
		}

		public void setStats(String stats) {
			this.stats = stats;
		}

		public String getNode() {
			return node;
		}

		public void setNode(String node) {
			this.node = node;
		}

		@Override
		public String toString() {
			return "Event [id=" + id + ", verb=" + verb + ", published=" + published + ", frameId=" + frameId
					+ ", version=" + version + ", service=" + service + ", source=" + source + ", stats=" + stats
					+ ", node=" + node + "]";
		}

	}

	public static class Payload {
		private String line;

		/**
		 * @return the line
		 */
		public String getLine() {
			return line;
		}

		/**
		 * @param line the line to set
		 */
		public void setLine(String line) {
			this.line = line;
		}

		@Override
		public String toString() {
			return "Payload [line=" + line + "]";
		}

	}

	public static class Generator {

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

		@Override
		public String toString() {
			return "Generator [source=" + source + ", eventId=" + eventId + "]";
		}

	}

	public static class Provider {
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

		@Override
		public String toString() {
			return "Provider [service=" + service + ", version=" + version + "]";
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

		@Override
		public String toString() {
			return "Actor [uid=" + uid + ", login=" + login + ", registrationStatus=" + registrationStatus + ", email="
					+ email + ", type=" + type + ", registrationTime=" + registrationTime + "]";
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

		@Override
		public String toString() {
			return "StreamObject [objectType=" + objectType + ", id=" + id + ", displayName=" + displayName
					+ ", content=" + content + ", visibility=" + visibility + ", subject=" + subject + ", added="
					+ added + ", postTime=" + postTime + ", isTopic=" + isTopic + "]";
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

		@Override
		public String toString() {
			return "Target [type=" + type + ", conversationType=" + conversationType + ", id=" + id
					+ ", conversationId=" + conversationId + "]";
		}

	}

	@Override
	public String toString() {
		return "ActivityStreamv1 [formatVersion=" + formatVersion + ", event=" + event + ", payload=" + payload
				+ ", generator=" + generator + ", provider=" + provider + ", actor=" + actor + ", streamObject="
				+ streamObject + ", target=" + target + "]";
	}

}