package com.sudosaints.android.mojoscribe.model;

import java.util.List;

public class UploadedFeed {
	private int id;
	private boolean checked = false;

	private String headline, updatedOn;
	private List<IdValue> files;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public List<IdValue> getFiles() {
		return files;
	}

	public void setFiles(List<IdValue> files) {
		this.files = files;
	}

}
