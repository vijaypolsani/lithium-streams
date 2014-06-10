package com.lithium.streams.compliance.model;

import java.util.Arrays;

public class LiaEvent {

	private String event;
	private String id;
	private String type;
	private String frameStart;
	private String time;
	private String frameId;
	private String version;
	private String service;
	private String source;
	private String node;
	private LiaEventPayload[] payload;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
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

	public String getFrameStart() {
		return frameStart;
	}

	public void setFrameStart(String frameStart) {
		this.frameStart = frameStart;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public LiaEventPayload[] getPayload() {
		return payload;
	}

	public void setPayload(LiaEventPayload[] payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "LiaEvent [event=" + event + ", id=" + id + ", type=" + type + ", frameStart=" + frameStart + ", time="
				+ time + ", frameId=" + frameId + ", version=" + version + ", service=" + service + ", source="
				+ source + ", node=" + node + ", payload=" + Arrays.toString(payload) + "]";
	}

}
