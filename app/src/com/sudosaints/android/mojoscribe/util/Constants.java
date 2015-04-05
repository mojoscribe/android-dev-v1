package com.sudosaints.android.mojoscribe.util;

public class Constants {
	
	public static final String DATABASE_NAME = "MojoScribeDB";
	
	public static final String GLOBAL_IMPACT = "Global Impact";
	public static final String NATIONAL_IMPACT = "National Impact";
	public static final String INTERNATIONAL_IMPACT = "Internaional Impact";
	public static final String URI_SCHEME = "http";
	
	public static final int REQUEST_LAUNCH_CAMERA = 10001;
	public static final int REQUEST_LAUNCH_GALLERY = 10002;
	public static final int REQUEST_LAUNCH_GALLERY_VIDEO = 10003;
	public static final int REQUEST_LAUNCH_CAMERA_VIDEO = 10004;
	public static final int REQUEST_SHARE_ON_TWITTER = 10005;
	public static final int REQUEST_SHARE_ON_GPLUS = 10006;
	
	public static final String MEDIA_FILE_NAME = "media.jpg";
	public static final String MEDIA_VIDEO_FILE_NAME = "vid.mp4";
	
	public static final int MEDIA_FILE_ROTATE_TIME_SEC=5;
	

	public static enum DrawerOptionAction {
		UPLOAD,
		HOME, 
		FEATURED, 
		CATEGOIES, 
		CATEGOIES_DISP, 
		POLLS, 
		ABOUT_US, 
		CONTACT_US, 
		LOGOUT, 
		RECENT_NEWS, 
		BREAKING, 
		MOJO_PICKS, 
		TRENDING_HASH_TAGS,
		TRENDING_HASH_TAGS_DISP,
		NEWSROOM, 
		POSTS, 
		DRAFTS, 
		SETTINGS, 
		LOGIN, 
		REGISTER,
		NOTIFICATIONS,
		FEEDBACK,
		ANONYMOUS,
		LOCATION_BASED
	}

	public static enum DrawerOptionColor {
		GREEN, 
		RED, 
		LITE_RED

	}

}
