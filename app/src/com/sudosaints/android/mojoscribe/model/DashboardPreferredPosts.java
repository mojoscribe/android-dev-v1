package com.sudosaints.android.mojoscribe.model;

import java.util.List;

public class DashboardPreferredPosts {
	private int id;
	private String name;
	private List<DashboardFeed> feeds;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DashboardFeed> getFeeds() {
		return feeds;
	}
	public void setFeeds(List<DashboardFeed> feeds) {
		this.feeds = feeds;
	}
	

}
