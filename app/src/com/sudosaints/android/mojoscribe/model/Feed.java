package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;
import java.util.List;

public class Feed implements Serializable {

	private float countRating;
	private int id, authorId;
	private boolean isVideo=false;
	private String ownerImageUrl, countViews, countShares, feedHeadLine;
	private String authorName, creationDate,imapctType;
	List<FeedFiles> files;

	
	public boolean isVideo() {
		return isVideo;
	}

	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	public String getImapctType() {
		return imapctType;
	}

	public void setImapctType(String imapctType) {
		this.imapctType = imapctType;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountShares() {
		return countShares;
	}

	public void setCountShares(String countShares) {
		this.countShares = countShares;
	}

	public String getFeedHeadLine() {
		return feedHeadLine;
	}

	public void setFeedHeadLine(String feedHeadLine) {
		this.feedHeadLine = feedHeadLine;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public List<FeedFiles> getFiles() {
		return files;
	}

	public void setFiles(List<FeedFiles> files) {
		this.files = files;
	}

	private int image, user;// for demo pourpose

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public int getUser() {
		return user;
	}

	public void setUser(int user) {
		this.user = user;
	}

	public Feed() {
		super();
	}

	public String getCountViews() {
		return countViews;
	}

	public void setCountViews(String countViews) {
		this.countViews = countViews;
	}

	public String getcountShares() {
		return countShares;
	}

	public void setcountShares(String countShares) {
		this.countShares = countShares;
	}

	public float getCountRating() {
		return countRating;
	}

	public void setCountRating(float countRating) {
		this.countRating = countRating;
	}

	public String getOwnerImageUrl() {
		return ownerImageUrl;
	}

	public void setOwnerImageUrl(String ownerImageUrl) {
		this.ownerImageUrl = ownerImageUrl;
	}

}
