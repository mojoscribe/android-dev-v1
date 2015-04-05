package com.sudosaints.android.mojoscribe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.sudosaints.android.mojoscribe.util.Logger;

public class Preferences {

	public static final int ACCESS_TOKEN_REFRESH_HOURS = 72;

	private static final String SERVER_URL = "SERVER_URL";
	private static final String ACCESS_TOKEN = "AUTH_TOKEN";
	private static final String ACCESS_TOKEN_TIMESTAMP = "ACCESS_TOKEN_TIMESTAMP";
	private static final String USER_ID = "USER_ID";
	private static final String USER_NAME = "USER_NAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String USER_EMAIL = "USER_EMAIL";
	private static final String GCM_ID = "GCM_ID";
	private static final String USER_FIRSTNAME = "USER_FIRSTNAME";
	private static final String USER_LASTNAME = "USER_LASTNAME";
	private static final String USER_URL = "USER_URL";
	private static final String ACTIVE_NOTIFICATION = "ACTIVE_NOTIFICATION";

	private static final String USER_COVER_PIC = "USER_COVER_PIC";
	private static final String USER_PROFILE_PIC = "USER_PROFILE_PIC";

	private static final String IS_FIRST_TIME_LOGIN = "IS_FIRST_TIME_LOGIN";

	private Context context;
	private Logger logger;

	public Preferences(Context context) {
		super();
		this.context = context;
		this.logger = new Logger(context);
	}

	protected SharedPreferences getSharedPreferences(String key) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	private String getString(String key, String def) {
		SharedPreferences prefs = getSharedPreferences(key);
		String s = prefs.getString(key, def);
		return s;
	}

	private int getInt(String key, int def) {
		SharedPreferences prefs = getSharedPreferences(key);
		int i = Integer.parseInt(prefs.getString(key, Integer.toString(def)));
		return i;
	}

	private float getFloat(String key, float def) {
		SharedPreferences prefs = getSharedPreferences(key);
		float f = Float.parseFloat(prefs.getString(key, Float.toString(def)));
		return f;
	}

	private long getLong(String key, long def) {
		SharedPreferences prefs = getSharedPreferences(key);
		long l = Long.parseLong(prefs.getString(key, Long.toString(def)));
		return l;
	}

	private void setString(String key, String val) {
		SharedPreferences prefs = getSharedPreferences(key);
		Editor e = prefs.edit();
		e.putString(key, val);
		e.commit();
	}

	private void setBoolean(String key, boolean val) {
		SharedPreferences prefs = getSharedPreferences(key);
		Editor e = prefs.edit();
		e.putBoolean(key, val);
		e.commit();
	}

	private void setInt(String key, int val) {
		SharedPreferences prefs = getSharedPreferences(key);
		Editor e = prefs.edit();
		e.putString(key, Integer.toString(val));
		e.commit();
	}

	private void setLong(String key, long val) {
		SharedPreferences prefs = getSharedPreferences(key);
		Editor e = prefs.edit();
		e.putString(key, Long.toString(val));
		e.commit();
	}

	private boolean getBoolean(String key, boolean def) {
		SharedPreferences prefs = getSharedPreferences(key);
		boolean b = prefs.getBoolean(key, def);
		return b;
	}

	private int[] getIntArray(String key, String def) {
		String s = getString(key, def);
		int[] ia = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			ia[i] = s.charAt(i) - '0';
		}
		return ia;
	}

	/*
	 * Public methods to get/set prefs
	 */

	public String getServerUrl() {
		return getString(SERVER_URL, context.getResources().getString(R.string.server_url));
	}

	public void setServerUrl(String serverUrl) {
		if (serverUrl.endsWith("/")) {
			serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
		}
		logger.debug("Server Url is - " + serverUrl);
		setString(SERVER_URL, serverUrl);
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return getString(ACCESS_TOKEN, null);
	}

	public void setAccessToken(String accessToken) {
		setString(ACCESS_TOKEN, accessToken);

	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return getLong(USER_ID, 0);
	}

	public void setId(long userId) {
		setLong(USER_ID, userId);
	}

	public String getUserName() {
		String userName = getString(USER_NAME, "");
		return userName;
	}

	public void setUserName(String userName) {
		setString(USER_NAME, userName);
	}

	public String getGcmId() {
		String gcmId = getString(GCM_ID, "");
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		setString(GCM_ID, gcmId);
	}

	public String getPassword() {
		return getString(PASSWORD, "");
	}

	public void setPassword(String password) {
		setString(PASSWORD, password);
	}

	public void setEmail(String email) {
		setString(USER_EMAIL, email);
	}

	public String getEmail() {
		return getString(USER_EMAIL, "");
	}

	public String getFullname() {
		return (getString(USER_FIRSTNAME, "") + " " + getString(USER_LASTNAME, ""));
	}

	public void setFirstname(String firstname) {
		setString(USER_FIRSTNAME, firstname);
	}

	public String getFirstname() {
		return getString(USER_FIRSTNAME, "");
	}

	public void setLastname(String lastname) {
		setString(USER_LASTNAME, lastname);
	}

	public String getLastname() {
		return getString(USER_LASTNAME, "");
	}

	public void setUrl(String url) {
		setString(USER_URL, url);
	}

	public String getUrl() {
		return getString(USER_URL, "");
	}

	public void setCoverPic(String path) {
		setString(USER_COVER_PIC, path);
	}

	public String getCoverPic() {
		return getString(USER_COVER_PIC, "");
	}

	public void setProfilePic(String path) {
		setString(USER_PROFILE_PIC, path);
	}

	public String getProfilePic() {
		return getString(USER_PROFILE_PIC, "");
	}

	public void setFirstTimeLogin(boolean val) {
		setBoolean(IS_FIRST_TIME_LOGIN, val);
	}

	public boolean getFirstTimeLogin() {
		return getBoolean(IS_FIRST_TIME_LOGIN, true);
	}

	public boolean getActiveNotification() {
		return getBoolean(ACTIVE_NOTIFICATION, false);
	}

	public void setActiveNotification(boolean val) {
		setBoolean(ACTIVE_NOTIFICATION, val);
	}

	public void logOut() {
		setString(USER_COVER_PIC, "");
		setString(USER_EMAIL, "");
		setString(USER_FIRSTNAME, "");
		setLong(USER_ID, 0);
		setString(USER_NAME, "");
		setString(USER_PROFILE_PIC, "");
		setString(USER_URL, "");
		setString(ACCESS_TOKEN, "");
		setString(GCM_ID, "");
		setBoolean(IS_FIRST_TIME_LOGIN, true);
		setBoolean(ACTIVE_NOTIFICATION, false);
	}

}
