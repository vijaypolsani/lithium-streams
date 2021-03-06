package com.lithium.streams.compliance.model;

public class Event {
	private String jsonContent;

	/**
	 * @return the jsonContent
	 */
	public String getJsonContent() {
		return jsonContent;
	}

	/**
	 * @param jsonContent the jsonContent to set
	 */
	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}

	@Override
	public String toString() {
		return "Event [jsonContent=" + jsonContent + "]";
	}

}
