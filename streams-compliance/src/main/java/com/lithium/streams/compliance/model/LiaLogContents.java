package com.lithium.streams.compliance.model;

public class LiaLogContents {
	private String fileName;
	private LiaEvent[] liaEvent;

	public LiaLogContents() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LiaEvent[] getLiaEvent() {
		return liaEvent;
	}

	public void setLiaEvent(LiaEvent[] liaEvent) {
		this.liaEvent = liaEvent;
	}

}
