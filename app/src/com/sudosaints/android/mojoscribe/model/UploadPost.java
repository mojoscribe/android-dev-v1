package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;
import java.util.List;

import android.net.Uri;

public class UploadPost implements Serializable {

	private int Id;
	private String headLine, description, hashTags;
	private String Source;
	private int category = -1, impact = -1;
	private String impactString;
	private List<Uri> files;
	private String userType = "user";
	private boolean isVideo = false;
	private String positionString;

	public UploadPost() {
		super();
	}

	public String getPositionString() {
		return positionString;
	}

	public void setPositionString(String positionString) {
		this.positionString = positionString;
	}

	public String getImpactString() {
		return impactString;
	}

	public void setImpactString(String impactString) {
		this.impactString = impactString;
	}

	public boolean isVideo() {
		return isVideo;
	}

	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getHeadLine() {
		return headLine;
	}

	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHashTags() {
		return hashTags;
	}

	public void setHashTags(String hashTags) {
		this.hashTags = hashTags;
	}

	public String getSource() {
		return Source;
	}

	public void setSource(String source) {
		Source = source;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getImpact() {
		return impact;
	}

	public void setImpact(int impact) {
		this.impact = impact;
	}

	public List<Uri> getFiles() {
		return files;
	}

	public void setFiles(List<Uri> files) {
		this.files = files;
	}

}
