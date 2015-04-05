package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class NewsRoomPosts implements Serializable{
	int id;
	String headLine;
	String postDate;
	List<IdValue> hashTags;
	String mediaUlr;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHeadLine() {
		return headLine;
	}
	public void setHeadLine(String headLine) {
		this.headLine = headLine;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public List<IdValue> getHashTags() {
		return hashTags;
	}
	public void setHashTags(List<IdValue> hashTags) {
		this.hashTags = hashTags;
	}
	public String getMediaUlr() {
		return mediaUlr;
	}
	public void setMediaUlr(String mediaUlr) {
		this.mediaUlr = mediaUlr;
	}
	

}
