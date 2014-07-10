package com.lithium.streams.compliance.model;

public class LiaEventPayload {

	private String actionId;
	private String actionKey;
	private LiaEventUser[] user;

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

	public LiaEventUser[] getUser() {
		return user;
	}

	public void setUser(LiaEventUser[] user) {
		this.user = user;
	}
}
