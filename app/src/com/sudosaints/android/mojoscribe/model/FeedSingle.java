package com.sudosaints.android.mojoscribe.model;

import java.util.List;

public class FeedSingle {

	private int id, authorId;
	private String imageUrl, feedHeadLine, authorName, authorImageUrl, cretionDate, feedDescription;
	private List<FeedFiles> files;
	private List<IdValue> hashTags;
	private String postType, loaction;
	private String viewsNoString, sharesNoString, impactType;
	private float avgRating;
	private boolean isVideo = false;
	private String linkString;
	private long time;

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getLoaction() {
		return loaction;
	}

	public void setLoaction(String loaction) {
		this.loaction = loaction;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getLinkString() {
		return linkString;
	}

	public void setLinkString(String linkString) {
		this.linkString = linkString;
	}

	public boolean isVideo() {
		return isVideo;
	}

	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}

	public float getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}

	public String getViewsNoString() {
		return viewsNoString;
	}

	public void setViewsNoString(String viewsNoString) {
		this.viewsNoString = viewsNoString;
	}

	public String getSharesNoString() {
		return sharesNoString;
	}

	public void setSharesNoString(String sharesNoString) {
		this.sharesNoString = sharesNoString;
	}

	public String getImpactType() {
		return impactType;
	}

	public void setImpactType(String impactType) {
		this.impactType = impactType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getAuthorImageUrl() {
		return authorImageUrl;
	}

	public void setAuthorImageUrl(String authorImageUrl) {
		this.authorImageUrl = authorImageUrl;
	}

	public String getCretionDate() {
		return cretionDate;
	}

	public void setCretionDate(String cretionDate) {
		this.cretionDate = cretionDate;
	}

	public String getFeedDescription() {
		return feedDescription;
	}

	public void setFeedDescription(String feedDescription) {
		this.feedDescription = feedDescription;
	}

	public List<FeedFiles> getFiles() {
		return files;
	}

	public void setFiles(List<FeedFiles> files) {
		this.files = files;
	}

	public String getPostType() {
		return postType;
	}

	public List<IdValue> getHashTags() {
		return hashTags;
	}

	public void setHashTags(List<IdValue> hashTags) {
		this.hashTags = hashTags;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

}
