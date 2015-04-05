package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NewsRoomSubscription implements Serializable {

	private String username,subscribers,imageUrl,repoterLevel;
	private int id;
	
	public NewsRoomSubscription() {
		super();
	}

	public String getRepoterLevel() {
		return repoterLevel;
	}

	public void setRepoterLevel(String repoterLevel) {
		this.repoterLevel = repoterLevel;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(String subscribers) {
		this.subscribers = subscribers;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

}
