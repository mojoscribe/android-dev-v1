package com.sudosaints.android.mojoscribe.model;

import java.util.List;

public class FeedDraft {
	private String headLine,updatedOn;
	private List<FeedFiles> files;
	public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	public List<FeedFiles> getFiles() {
		return files;
	}
	public void setFiles(List<FeedFiles> files) {
		this.files = files;
	}

}
