package com.sudosaints.android.mojoscribe.util;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class ApiRequest implements Serializable {

	public enum RequestMethod {
		GET, POST, POST_RAW, FILE_UPLOAD, PUT, DELETE, POST_JSON
	};

	public enum RequestName {
		USER_LOGIN,
		USER_FB_LOGIN,
		USER_GPLUS_LOGIN,
		USER_SIGNUP, 
		GET_USER_DETAILS, 
		EDIT_USER_DETAILS, 
		GET_LANDING_FEEDS, 
		GET_FEED, 
		POST_FEED, 
		GET_PRE_POST_DATA, 
		UPLOAD_POST, 
		DRAFT_POST,
		SEARCH,
		GET_NEWSROOM_DATA,
		DO_SUBSCRIBE,
		GET_PUBLISH_DATA,
		DO_DELETE_POST,
		GET_DRAFT_DATA,
		DO_PUBLISH,
		DO_UNPUBLISH,
		GET_DASHBOARD_DATA,
		DO_POST_RATING,
		DO_POST_IMPACT_TYPE,
		GET_USER_SETTINGS,
		SET_PROFILE_SETTINGS,
		SET_PREFERANCE_SETTINGS,
		GET_CATEGORIES,
		GET_CATEGORIES_FEEDS,
		GET_CATEGORIES_PAGINATE,
		CONTACT_US,
		COUNTRY_AUTOCOMPLETE,
		CITY_AUTOCOMPLETE,
		RECENT_NEWS,
		BREAKING,
		MOJO_PICKS,
		GET_POLLS,
		SET_POLL,
		GET_PREF_PLACES,
		GET_NOTIFICATION,
		GET_TRENDING_HASHTAGS,
		GET_TRENDING_HASHTAGS_FEEDS,
		DO_FEEDS_PAGINATION,
		DO_SEARCH_PAGINATION,
		DO_RECENT_PAGINATION,
		DO_RECENT_FEEDS_PAGINATION,
		DO_MOJOPICKS_FEEDS_PAGINATION,
		DO_BREAKING_PAGINATION,
		DO_SHARE_SINGEL_FEED,
		ANONYMOUS,
		ANANYMOUS_PAGINATION,
		GET_LOCATION_FEEDS,
		DO_LOCATION_FEEDS_PAGINATION
		
	}

	private RequestName requestName;
	private RequestMethod requestMethod;
	private String url;
	private Properties reqParams = new Properties();
	private boolean isUrlAbsolute = false;
	private boolean useBasicAuth = false;
	private InputStream postDataStream;
	private int apiVersion;
	private String[] pathVariables;
	private JSONObject jsonObject;

	private boolean addAccessTokenHeaders = true;
	private List<PostFile> postFiles = new ArrayList<ApiRequest.PostFile>();
	private File postFile;
	private String postFileAttrName;

	private Properties postUrlParams = new Properties();

	public ApiRequest(boolean addAccessTokenHeader) {
		apiVersion = 1;
		requestMethod = RequestMethod.GET;
		this.addAccessTokenHeaders = addAccessTokenHeader;
	}

	public ApiRequest(RequestName reqName, String[] pathVariables) {
		this(true);
		this.pathVariables = pathVariables;
		setRequestName(reqName);
	}

	public RequestName getRequestName() {
		return requestName;
	}

	public ApiRequest setRequestName(RequestName requestName) {
		this.requestName = requestName;
		switch (requestName) {

		case USER_LOGIN:
			url = "/login";
			break;
		case USER_SIGNUP:
			url = "/register";
			break;
		case GET_LANDING_FEEDS:
			url = "/mainPage";
			break;
		case GET_FEED:
			url = "/singlePost";
			break;
		case POST_FEED:
			url = "/postFeed";
			break;
		case GET_PRE_POST_DATA:
			url = "/prePostData";
			break;
		case UPLOAD_POST:
			url = "/post";
			break;
		case SEARCH:
			url = "/search";
			break;
		case GET_NEWSROOM_DATA:
			url = "/newsRoom";
			break;
		case DO_SUBSCRIBE:
			url = "/follow";
			break;
		case GET_PUBLISH_DATA:
			url = "/postsList";
			break;
		case DO_DELETE_POST:
			url = "/posts/delete";
			break;
		case DO_PUBLISH:
			url = "/drafts/publish";
			break;
		case DO_UNPUBLISH:
			url = "/postsList/unPublish";
			break;
		case GET_DRAFT_DATA:
			url = "/drafts";
			break;
		case GET_DASHBOARD_DATA:
			url = "/dashboard";
			break;
		case DO_POST_RATING:
			url = "/singlePost/saveRating";
			break;
		case DO_POST_IMPACT_TYPE:
			url = "/singlePost/impact";
			break;
		case GET_USER_SETTINGS:
			url = "/settings/preData";
			break;
		case SET_PREFERANCE_SETTINGS:
			url = "/settings/preferences";
			break;
		case SET_PROFILE_SETTINGS:
			url = "/settings/profile";
			break;
		case GET_CATEGORIES:
			url = "/categories";
			break;
		case GET_CATEGORIES_PAGINATE:
			url = "/categories/paginate";
			break;
		case GET_CATEGORIES_FEEDS:
			url = "/categories/posts";
			break;
		case USER_FB_LOGIN:
			url = "/fbLogin";
			break;
		case USER_GPLUS_LOGIN:
			url = "/gPlus";
			break;
		case CONTACT_US:
			url = "/contact";
			break;
		case COUNTRY_AUTOCOMPLETE:
			url = "/places/country";
			break;
		case CITY_AUTOCOMPLETE:
			url = "/places/city";
			break;
		case RECENT_NEWS:
			url = "/recent";
			break;
		
		case BREAKING:
			url = "/mainPage";
			break;
		case MOJO_PICKS:
			url = "/featured";
			break;
		case GET_POLLS:
			url = "/polls";
			break;
		case SET_POLL:
			url = "/polls/submit";
			break;
		case GET_PREF_PLACES:
			url = "/places/locations";
			break;
		case GET_NOTIFICATION:
			url = "/notifications";
			break;
		case GET_TRENDING_HASHTAGS:
			url = "/trendingHashtags";
			break;
		case GET_TRENDING_HASHTAGS_FEEDS:
			url = "/trendingHashtags/posts";
			break;
		case DO_FEEDS_PAGINATION:
			url = "/mainPage/paginate";
			break;
		case DO_SEARCH_PAGINATION:
			url="/search/paginate";
			break;
		case DO_RECENT_FEEDS_PAGINATION:
			url = "/recent/paginate";
			break;
		case DO_MOJOPICKS_FEEDS_PAGINATION:
			url = "/featured/paginate";
			break;
		case DRAFT_POST:
			url = "/drafts/save";
			break;
		case DO_SHARE_SINGEL_FEED:
			url="/singlePost/share";
			break;
		case ANONYMOUS:
			url="/anonymousPage";
			break;
		case ANANYMOUS_PAGINATION:
			url="/anonymousPage/paginate";
			break;
		case GET_LOCATION_FEEDS:
			url="/getPostsByLocation";
			break;
		case DO_LOCATION_FEEDS_PAGINATION:
			url="/getPostsByLocation/paginate";
			break;
			
		default:
			break;
		}

		return this;
	}

	public ApiRequest setRequestMethod(RequestMethod method) {
		this.requestMethod = method;
		return this;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public ApiRequest setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public ApiRequest setReqParam(String name, String value) {
		if (null != value) {
			reqParams.setProperty(name, value);
		}
		return this;
	}

	public Properties getReqParams() {
		return reqParams;
	}

	public boolean isUrlAbsolute() {
		return isUrlAbsolute;
	}

	public ApiRequest setUrlAbsolute(boolean isUrlAbsolute) {
		this.isUrlAbsolute = isUrlAbsolute;
		return this;
	}

	public boolean isUseBasicAuth() {
		return useBasicAuth;
	}

	public void setUseBasicAuth(boolean useBasicAuth) {
		this.useBasicAuth = useBasicAuth;
	}

	public InputStream getPostDataStream() {
		return postDataStream;
	}

	public ApiRequest setPostDataStream(InputStream postDataStream) {
		this.postDataStream = postDataStream;
		return this;
	}

	public File getPostFile() {
		return postFile;
	}

	public ApiRequest setPostFile(File postFile, String attrName) {
		this.postFile = postFile;
		this.postFileAttrName = attrName;
		return this;
	}

	public int getApiVersion() {
		return apiVersion;
	}

	public String[] getPathVariables() {
		return pathVariables;
	}

	public void setPathVariables(String[] pathVariables) {
		this.pathVariables = pathVariables;
		setRequestName(requestName);
	}

	public String getPostFileAttrName() {
		return postFileAttrName;
	}

	public void setPostFileAttrName(String postFileAttrName) {
		this.postFileAttrName = postFileAttrName;
	}

	public boolean isAddAccessTokenHeaders() {
		return addAccessTokenHeaders;
	}

	public void setAddAccessTokenHeaders(boolean addAccessTokenHeaders) {
		this.addAccessTokenHeaders = addAccessTokenHeaders;
	}

	/**
	 * @return the jsonObject
	 */
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	/**
	 * @param jsonObject
	 *            the jsonObject to set
	 */
	public void setJsonObject(JSONObject jsonObject) {
		this.setRequestMethod(RequestMethod.POST_JSON);
		this.jsonObject = jsonObject;
	}

	public void addPostFile(PostFile postFile) {
		if (null == postFiles) {
			postFiles = new ArrayList<ApiRequest.PostFile>();
		}
		postFiles.add(postFile);
	}

	public List<PostFile> getPostFiles() {
		return this.postFiles;
	}

	public void addPostUrlParam(String name, String value) {

		if (null == postUrlParams)
			postUrlParams = new Properties();
		postUrlParams.put(name, value);
	}

	public void setPostUrlParams(List<NameValuePair> urlParams) {

		if (null == postUrlParams)
			postUrlParams = new Properties();

		for (NameValuePair nameValuePair : urlParams) {
			postUrlParams.put(nameValuePair.getName(), nameValuePair.getValue());
		}
	}

	public Properties getPostUrlParams() {

		return postUrlParams;
	}

	static class PostFile implements Serializable {

		private String fileName;
		private File postFile;

		/**
		 * @param fileName
		 * @param postFile
		 */
		public PostFile(String fileName, File postFile) {
			super();
			this.fileName = fileName;
			this.postFile = postFile;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public File getPostFile() {
			return postFile;
		}

		public void setPostFile(File postFile) {
			this.postFile = postFile;
		}
	}

}
