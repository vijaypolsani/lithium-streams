package com.lithium.streams.compliance.consumer;


public class Event {
	private byte[] jsonContent;

	public void setJsonContent(byte[] jsonContent) {
		this.jsonContent = jsonContent;
	}

	public byte[] getJsonContent() {
		return jsonContent;
	}

	@Override
	public String toString() {
		return "Event [jsonContent=" + jsonContent + "]";
	}

}
