package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;
import java.util.List;

public class DashboardFeed  implements Serializable{

	private int id;
	private String author,headline,rating,views;
	private List<IdValue> files;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	public List<IdValue> getFiles() {
		return files;
	}
	public void setFiles(List<IdValue> files) {
		this.files = files;
	}
	
}
