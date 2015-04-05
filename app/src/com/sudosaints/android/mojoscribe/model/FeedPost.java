package com.sudosaints.android.mojoscribe.model;

public class FeedPost {
	
	private String postContentString;
	private boolean postByMe;
	private String postTitle,postDescription,postCategory,postImpact;
	private String postHastTags;
	
	public String getPostContentString() {
		return postContentString;
	}
	public void setPostContentString(String postContentString) {
		this.postContentString = postContentString;
	}
	public boolean isPostByMe() {
		return postByMe;
	}
	public void setPostByMe(boolean postByMe) {
		this.postByMe = postByMe;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostDescription() {
		return postDescription;
	}
	public void setPostDescription(String postDescription) {
		this.postDescription = postDescription;
	}
	public String getPostCategory() {
		return postCategory;
	}
	public void setPostCategory(String postCategory) {
		this.postCategory = postCategory;
	}
	public String getPostImpact() {
		return postImpact;
	}
	public void setPostImpact(String postImpact) {
		this.postImpact = postImpact;
	}
	public String getPostHastTags() {
		return postHastTags;
	}
	public void setPostHastTags(String postHastTags) {
		this.postHastTags = postHastTags;
	}
	

}
