package com.lithium.streams.compliance.model;

import java.sql.Timestamp;

public class ActivityStreams {
	private String version = "1.0";
	private String published = "";
	private final Generator generator = new Generator();
	private final Provider provider = new Provider();
	private String extensionElements = "";
	private String title = "";
	private final Actor actor = new Actor();
	private String verb = "";
	private final Object obj = new Object();

	private final Target target = new Target();

	public class Generator {

		private String source = "";

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}

	public class Provider {
		private String service = "";

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

	}

	public class Actor {

		private String id = "";
		private String login = "";
		private String registrationStatus = "";
		private String email = "";
		private String uid = "";
		private String type = "";
		private String frameId = "";
		private String typeRanking = "";
		private String model = "";
		private String name = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
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

		public String getFrameId() {
			return frameId;
		}

		public void setFrameId(String frameId) {
			this.frameId = frameId;
		}

		public String getTypeRanking() {
			return typeRanking;
		}

		public void setTypeRanking(String typeRanking) {
			this.typeRanking = typeRanking;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

	}

	public class Object {
		private String actionId = "";
		private String actionKey = "";
		private String actionResult = "";

		public String getActionId() {
			return actionId;
		}

		public void setActionId(String actionId) {
			this.actionId = actionId;
		}

		public String getActionKey() {
			return actionKey;
		}

		public void setActionKey(String actionKey) {
			this.actionKey = actionKey;
		}

		public String getActionResult() {
			return actionResult;
		}

		public void setActionResult(String actionResult) {
			this.actionResult = actionResult;
		}
	}

	public class Target {

		private String version = "";
		private String type = "";
		private String source = "";
		private String service = "";
		private String node = "";
		private String num = "";
		private String visibility = "";
		private String subject = "";
		private String post_time = "";
		private String conversationStype = "";
		private String isTopic = "";
		private String message_type = "";

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public String getNode() {
			return node;
		}

		public void setNode(String node) {
			this.node = node;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getVisibility() {
			return visibility;
		}

		public void setVisibility(String visibility) {
			this.visibility = visibility;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getPost_time() {
			return post_time;
		}

		public void setPost_time(String post_time) {
			this.post_time = post_time;
		}

		public String getConversationStype() {
			return conversationStype;
		}

		public void setConversationStype(String conversationStype) {
			this.conversationStype = conversationStype;
		}

		public String getIsTopic() {
			return isTopic;
		}

		public void setIsTopic(String isTopic) {
			this.isTopic = isTopic;
		}

		public String getMessage_type() {
			return message_type;
		}

		public void setMessage_type(String message_type) {
			this.message_type = message_type;
		}

	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getExtensionElements() {
		return extensionElements;
	}

	public void setExtensionElements(String extensionElements) {
		this.extensionElements = extensionElements;
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

	public Object getObj() {
		return obj;
	}

	public Target getTarget() {
		return target;
	}

}
