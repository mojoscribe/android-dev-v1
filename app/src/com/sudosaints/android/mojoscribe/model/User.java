package com.sudosaints.android.mojoscribe.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.apache.http.entity.SerializableEntity;

import android.R.string;

public class User implements Serializable, Cloneable {

	private long id;
	private String username, password, email, imageUrl;
	private String firstName, lastName;
	private String joinDate, about, gender, location, country, city;
	private String postUrl;
	private boolean isFollowed;
	private List<SettingsCategoriesOption> categoriesOptions;
	private List<String> preferanceLocation;
	private boolean mobileNotification, emailNotification;
	private String contactNo;
	private String subscribers, repoterLevel;

	public String getRepoterLevel() {
		return repoterLevel;
	}

	public void setRepoterLevel(String repoterLevel) {
		this.repoterLevel = repoterLevel;
	}

	public String getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(String subscribers) {
		this.subscribers = subscribers;
	}

	public User() {
		super();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public User getClone() {
		return (User) SerializationUtils.clone(this);
	}

	public User(long id, String name, String password, String email, String imageUrl, String firstName, String lastName) {
		super();
		this.id = id;
		this.username = name;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imageUrl = imageUrl;

	}

	public User(long id, String name, String fullname, String imageUrl, String postUrl, String joinDate, String about, String gender, String location, boolean isFollowed) {
		super();
		this.id = id;
		this.username = name;
		this.firstName = fullname;
		this.imageUrl = imageUrl;
		this.joinDate = joinDate;
		this.about = about;
		this.gender = gender;
		this.location = location;
		this.isFollowed = isFollowed;
		this.postUrl = postUrl;

	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public List<SettingsCategoriesOption> getCategoriesOptions() {
		return categoriesOptions;
	}

	public void setCategoriesOptions(List<SettingsCategoriesOption> categoriesOptions) {
		this.categoriesOptions = categoriesOptions;
	}

	public List<String> getPreferanceLocation() {
		return preferanceLocation;
	}

	public void setPreferanceLocation(List<String> preferanceLocation) {
		this.preferanceLocation = preferanceLocation;
	}

	public boolean isMobileNotification() {
		return mobileNotification;
	}

	public void setMobileNotification(boolean mobileNotification) {
		this.mobileNotification = mobileNotification;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
