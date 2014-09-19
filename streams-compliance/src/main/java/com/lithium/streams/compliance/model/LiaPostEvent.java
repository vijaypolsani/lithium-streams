package com.lithium.streams.compliance.model;

public class LiaPostEvent implements ComplianceEvent {
	private final String liaPostContent;

	public LiaPostEvent(String liaPostContent) {
		this.liaPostContent = liaPostContent;
	}

	@Override
	public String getEvent() {
		return liaPostContent;
	}

	@Override
	public String toString() {
		return "LiaPostEvent [liaPostContent=" + new String(liaPostContent) + "]";
	}

}
