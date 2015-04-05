package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;

public class Notification implements Serializable {

	int id;
	String notificationText, imageUrl;
	String actionTypeString="";
	int actionId=0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getActionTypeString() {
		return actionTypeString;
	}

	public void setActionTypeString(String actionTypeString) {
		this.actionTypeString = actionTypeString;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public Notification() {
		super();
	}

}
