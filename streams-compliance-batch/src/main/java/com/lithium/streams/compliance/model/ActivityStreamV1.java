package com.lithium.streams.compliance.model;

public class ActivityStreamV1 {

	private final String formatVersion = "1.0";
	private String title = "";
	private String published = "";
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

	public Generator getGenerator() {
		return generator;
	}

	public Provider getProvider() {
		return provider;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Actor getActor() {
		return actor;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public Target getTarget() {
		return target;
	}

	public StreamObject getStreamObject() {
		return streamObject;
	}

	public class Generator {

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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
			result = prime * result + ((source == null) ? 0 : source.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Generator other = (Generator) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (eventId == null) {
				if (other.eventId != null)
					return false;
			} else if (!eventId.equals(other.eventId))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			return true;
		}

		private ActivityStreamV1 getOuterType() {
			return ActivityStreamV1.this;
		}

	}

	public class Provider {
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((service == null) ? 0 : service.hashCode());
			result = prime * result + ((version == null) ? 0 : version.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Provider other = (Provider) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (service == null) {
				if (other.service != null)
					return false;
			} else if (!service.equals(other.service))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}

		private ActivityStreamV1 getOuterType() {
			return ActivityStreamV1.this;
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((email == null) ? 0 : email.hashCode());
			result = prime * result + ((login == null) ? 0 : login.hashCode());
			result = prime * result + ((registrationStatus == null) ? 0 : registrationStatus.hashCode());
			result = prime * result + ((registrationTime == null) ? 0 : registrationTime.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((uid == null) ? 0 : uid.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Actor other = (Actor) obj;
			if (email == null) {
				if (other.email != null)
					return false;
			} else if (!email.equals(other.email))
				return false;
			if (login == null) {
				if (other.login != null)
					return false;
			} else if (!login.equals(other.login))
				return false;
			if (registrationStatus == null) {
				if (other.registrationStatus != null)
					return false;
			} else if (!registrationStatus.equals(other.registrationStatus))
				return false;
			if (registrationTime == null) {
				if (other.registrationTime != null)
					return false;
			} else if (!registrationTime.equals(other.registrationTime))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			if (uid == null) {
				if (other.uid != null)
					return false;
			} else if (!uid.equals(other.uid))
				return false;
			return true;
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((added == null) ? 0 : added.hashCode());
			result = prime * result + ((content == null) ? 0 : content.hashCode());
			result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((isTopic == null) ? 0 : isTopic.hashCode());
			result = prime * result + ((objectType == null) ? 0 : objectType.hashCode());
			result = prime * result + ((postTime == null) ? 0 : postTime.hashCode());
			result = prime * result + ((subject == null) ? 0 : subject.hashCode());
			result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StreamObject other = (StreamObject) obj;
			if (added == null) {
				if (other.added != null)
					return false;
			} else if (!added.equals(other.added))
				return false;
			if (content == null) {
				if (other.content != null)
					return false;
			} else if (!content.equals(other.content))
				return false;
			if (displayName == null) {
				if (other.displayName != null)
					return false;
			} else if (!displayName.equals(other.displayName))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (isTopic == null) {
				if (other.isTopic != null)
					return false;
			} else if (!isTopic.equals(other.isTopic))
				return false;
			if (objectType == null) {
				if (other.objectType != null)
					return false;
			} else if (!objectType.equals(other.objectType))
				return false;
			if (postTime == null) {
				if (other.postTime != null)
					return false;
			} else if (!postTime.equals(other.postTime))
				return false;
			if (subject == null) {
				if (other.subject != null)
					return false;
			} else if (!subject.equals(other.subject))
				return false;
			if (visibility == null) {
				if (other.visibility != null)
					return false;
			} else if (!visibility.equals(other.visibility))
				return false;
			return true;
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((conversationId == null) ? 0 : conversationId.hashCode());
			result = prime * result + ((conversationType == null) ? 0 : conversationType.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Target other = (Target) obj;
			if (conversationId == null) {
				if (other.conversationId != null)
					return false;
			} else if (!conversationId.equals(other.conversationId))
				return false;
			if (conversationType == null) {
				if (other.conversationType != null)
					return false;
			} else if (!conversationType.equals(other.conversationType))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((formatVersion == null) ? 0 : formatVersion.hashCode());
		result = prime * result + ((generator == null) ? 0 : generator.hashCode());
		result = prime * result + ((streamObject == null) ? 0 : streamObject.hashCode());
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + ((published == null) ? 0 : published.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((verb == null) ? 0 : verb.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityStreamV1 other = (ActivityStreamV1) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (formatVersion == null) {
			if (other.formatVersion != null)
				return false;
		} else if (!formatVersion.equals(other.formatVersion))
			return false;
		if (generator == null) {
			if (other.generator != null)
				return false;
		} else if (!generator.equals(other.generator))
			return false;
		if (streamObject == null) {
			if (other.streamObject != null)
				return false;
		} else if (!streamObject.equals(other.streamObject))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		if (published == null) {
			if (other.published != null)
				return false;
		} else if (!published.equals(other.published))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (verb == null) {
			if (other.verb != null)
				return false;
		} else if (!verb.equals(other.verb))
			return false;
		return true;
	}
}