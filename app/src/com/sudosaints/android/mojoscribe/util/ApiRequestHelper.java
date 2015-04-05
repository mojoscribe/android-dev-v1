package com.sudosaints.android.mojoscribe.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.net.Uri;

import com.sudosaints.android.mojoscribe.Preferences;
import com.sudosaints.android.mojoscribe.exception.CommunicationException;
import com.sudosaints.android.mojoscribe.model.FeedPost;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.model.SettingsCategoriesOption;
import com.sudosaints.android.mojoscribe.model.UploadPost;
import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.util.ApiRequest.PostFile;
import com.sudosaints.android.mojoscribe.util.ApiRequest.RequestMethod;
import com.sudosaints.android.mojoscribe.util.ApiRequest.RequestName;
import com.sudosaints.android.mojoscribe.util.ApiResponse.ApiError;

public class ApiRequestHelper {
	HttpHelper httpHelper;
	ResponseHelper responseHelper;
	Preferences preferences;

	Logger logger;

	public ApiRequestHelper(Context context) {
		super();
		httpHelper = new HttpHelper(context);
		responseHelper = new ResponseHelper(context);
		logger = new Logger(context);
		preferences = new Preferences(context);
	}

	@SuppressWarnings(value = "unused")
	private ApiResponse performApiRequest(ApiRequest apiRequest) {
		ApiResponse apiResponse = new ApiResponse().setSuccess(false);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		apiResponse = responseHelper.getApiResponse(response);
		return apiResponse;
	}

	public ApiResponse doRegisterUser(User user) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.USER_SIGNUP);
		apiRequest.setReqParam("userName", user.getUsername());
		apiRequest.setReqParam("password", user.getPassword());
		apiRequest.setReqParam("email", user.getEmail());

		/*
		 * apiRequest.setParam("shortDescription", user.getShortDescription());
		 * apiRequest.setParam("phoneNo", user.getPhoneNo());
		 * apiRequest.setParam("url",user.getUrl());
		 * apiRequest.setParam("tags",user.getTags());
		 */

		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doRegisterTruckUser(User user) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.FILE_UPLOAD);
		apiRequest.setRequestName(RequestName.USER_SIGNUP);
		apiRequest.setReqParam("username", user.getUsername());
		apiRequest.setReqParam("password", user.getPassword());
		apiRequest.setReqParam("email", user.getEmail());
		apiRequest.setPostFile(new File(user.getImageUrl()), "image");

		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, false);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doLogin(String email, String password) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.USER_LOGIN);
		apiRequest.setReqParam("gcmId", preferences.getGcmId());
		apiRequest.setReqParam("userName", email);
		apiRequest.setReqParam("password", password);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doConatctUs(String name, String email, String phone, String msg) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.CONTACT_US);
		apiRequest.setReqParam("name", name);
		apiRequest.setReqParam("email", email);
		apiRequest.setReqParam("phone", phone);
		apiRequest.setReqParam("message", msg);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doFBLogin(User user) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.USER_FB_LOGIN);

		apiRequest.setReqParam("id", user.getId() + "");
		apiRequest.setReqParam("gcmId", preferences.getGcmId());
		apiRequest.setReqParam("email", user.getEmail());
		apiRequest.setReqParam("firstName", user.getFirstName());
		apiRequest.setReqParam("lastName", user.getLastName());
		apiRequest.setReqParam("gender", user.getGender());

		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doGPlusLogin(User user) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.USER_GPLUS_LOGIN);

		apiRequest.setReqParam("name", user.getUsername());
		apiRequest.setReqParam("email", user.getEmail());
		apiRequest.setReqParam("gcmId", preferences.getGcmId());
		apiRequest.setReqParam("imageUrl", user.getImageUrl());

		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doMainPageFeeds() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_LANDING_FEEDS);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doFeedsPagination(String idsCSV) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_FEEDS_PAGINATION);
		apiRequest.setReqParam("postIds", idsCSV);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doLocationBasedPagination(String idsCSV) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_LOCATION_FEEDS_PAGINATION);
		apiRequest.setReqParam("postIds", idsCSV);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doLocationBasedFeeds() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_LOCATION_FEEDS);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doAnonymousPagination(String idsCSV) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.ANANYMOUS_PAGINATION);
		apiRequest.setReqParam("postIds", idsCSV);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doSearchPagination(String idsCSV, String query) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_SEARCH_PAGINATION);
		apiRequest.setReqParam("postIds", idsCSV);
		apiRequest.setReqParam("q", query);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doMojoPicksFeedsPagination(String idsCSV) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_MOJOPICKS_FEEDS_PAGINATION);
		apiRequest.setReqParam("postIds", idsCSV);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doRecentPagination(String idsCSV) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_RECENT_FEEDS_PAGINATION);
		apiRequest.setReqParam("postIds", idsCSV);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse getNotifications() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_NOTIFICATION);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse getpolls() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_POLLS);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse setPoll(int pollId, int optionId) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.SET_POLL);
		apiRequest.setReqParam("pollId", pollId + "");
		apiRequest.setReqParam("optionId", optionId + "");

		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doRecentFeeds() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.RECENT_NEWS);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doBreakingFeeds() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.BREAKING);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doAnonymousFeeds() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.ANONYMOUS);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doMojoFeeds() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.MOJO_PICKS);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doCategoriesFeeds(int categId) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_CATEGORIES_FEEDS);
		apiRequest.setReqParam("categId", categId + "");
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doCategoriesFeedsPaginate(int categId, String idsCSV) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_CATEGORIES_PAGINATE);
		apiRequest.setReqParam("categId", categId + "");
		apiRequest.setReqParam("postIds", idsCSV);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doTrendingHashTagsFeeds(String hashTag) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.SEARCH);
		apiRequest.setReqParam("q", hashTag);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doSearchFeeds(String query) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.SEARCH);
		apiRequest.setReqParam("q", query);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);

		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doSubscribe(int userId) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_SUBSCRIBE);
		apiRequest.setReqParam("userId", userId + "");
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);

		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse getPublishFeedData() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_PUBLISH_DATA);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);

		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse getDraftFeedData() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_DRAFT_DATA);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);

		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse getNewsRoomData(int userId) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_NEWSROOM_DATA);
		apiRequest.setReqParam("userId", userId + "");
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);

		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doFeedSingle(int id, boolean isPostViewed) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_FEED);
		ServerResponse response = null;
		apiRequest.setReqParam("postId", id + "");
		apiRequest.setReqParam("isViewed", String.valueOf(((isPostViewed) ? 1 : 0)));
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doShareSingle(int id) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.DO_SHARE_SINGEL_FEED);
		ServerResponse response = null;
		apiRequest.setReqParam("id", id + "");
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse getUserSettings() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_USER_SETTINGS);

		ServerResponse response = null;
		//

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse getCountriesAutocomplete(String q) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.COUNTRY_AUTOCOMPLETE);

		ServerResponse response = null;
		apiRequest.setReqParam("q", q);

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse getPrefPlacesAutocomplete(String q) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_PREF_PLACES);

		ServerResponse response = null;
		apiRequest.setReqParam("q", q);

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse getCityAutocomplete(String q, String country) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.CITY_AUTOCOMPLETE);

		ServerResponse response = null;
		apiRequest.setReqParam("q", q);
		apiRequest.setReqParam("country", country);

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse getCategories() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_CATEGORIES);

		ServerResponse response = null;
		//

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse getTrendingHashTags() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_TRENDING_HASHTAGS);

		ServerResponse response = null;
		//

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	// ///////////////////////////////
	public ApiResponse setProfileSettings() {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.SET_PROFILE_SETTINGS);

		ServerResponse response = null;
		//

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse setPreferanceSettings(User user) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.SET_PREFERANCE_SETTINGS);

		ServerResponse response = null;
		String categories = "";
		for (SettingsCategoriesOption opt : user.getCategoriesOptions()) {
			if (opt.isSelected())
				categories += (opt.getId() + ",");
		}
		apiRequest.setReqParam("categories", categories);
		String locations = "";
		for (String opt : user.getPreferanceLocation()) {

			locations += (opt + ",");
		}
		apiRequest.setReqParam("locations", locations);
		apiRequest.setReqParam("mobileNotification", String.valueOf(user.isMobileNotification()));
		apiRequest.setReqParam("emailNotification", String.valueOf(user.isEmailNotification()));

		//

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	// ///////////////////////////

	public ApiResponse getDashboardData() {

		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setRequestName(RequestName.GET_DASHBOARD_DATA);
		ServerResponse response = null;
		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse registerUser(FeedPost feedPost) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(false);
		apiRequest.setRequestMethod(RequestMethod.FILE_UPLOAD);
		apiRequest.setRequestName(RequestName.POST_FEED);
		apiRequest.setReqParam("title", feedPost.getPostTitle());
		apiRequest.setReqParam("description", feedPost.getPostDescription());
		apiRequest.setReqParam("category", feedPost.getPostCategory());
		apiRequest.setReqParam("impact", feedPost.getPostImpact());
		apiRequest.setReqParam("hashTags", feedPost.getPostHastTags());

		if (null != feedPost.getPostContentString()) {
			apiRequest.addPostFile(new PostFile("profilePic", new File(feedPost.getPostContentString())));
		}

		ServerResponse serverResponse = null;
		try {
			serverResponse = httpHelper.sendRequest(apiRequest);
			apiResponse = responseHelper.getApiResponse(serverResponse);
		} catch (CommunicationException e) {
			e.printStackTrace();
			apiResponse.setError(ApiError.COMMUNICATION_ERROR);
		}
		return apiResponse;
	}

	public ApiResponse getPrePostData(String authToken) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);

		apiRequest.setRequestName(RequestName.GET_PRE_POST_DATA);
		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doPostRating(int postId, float ratingValue) {

		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);

		apiRequest.setRequestName(RequestName.DO_POST_RATING);
		ServerResponse response = null;
		apiRequest.setReqParam("postId", postId + "");
		apiRequest.setReqParam("rating", ratingValue + "");

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doPostImpact(int postId, int impactId) {

		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);

		apiRequest.setRequestName(RequestName.DO_POST_IMPACT_TYPE);
		ServerResponse response = null;
		apiRequest.setReqParam("postId", postId + "");
		apiRequest.setReqParam("impactId", impactId + "");

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse doUploadPost(UploadPost post) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.FILE_UPLOAD);
		apiRequest.setRequestName(RequestName.UPLOAD_POST);
		if (post.getId() == 0) {
			apiRequest.setReqParam("id", "");
		} else {
			apiRequest.setReqParam("id", post.getId() + "");
		}

		apiRequest.setReqParam("headline", post.getHeadLine());
		apiRequest.setReqParam("description", post.getDescription());
		apiRequest.setReqParam("category", post.getCategory() + "");
		apiRequest.setReqParam("impact", post.getImpact() + "");
		apiRequest.setReqParam("hashtags", post.getHashTags());
		apiRequest.setReqParam("userType", post.getUserType());
		apiRequest.setReqParam("position", post.getPositionString());

		// apiRequest.setReqParam("draftId", post.getDraftId()+"");
		apiRequest.setReqParam("source", post.getUserType());// ////////////////////////////////////////

		if (null != post.getFiles() && !post.isVideo()) {
			for (Uri uri : post.getFiles()) {
				apiRequest.setReqParam("postType", "Image");
				apiRequest.addPostFile(new PostFile("images[]", new File(uri.toString())));

			}

		} else if (null != post.getFiles() && post.isVideo()) {
			for (Uri uri : post.getFiles()) {
				apiRequest.addPostFile(new PostFile("video", new File(uri.toString())));
				apiRequest.setReqParam("postType", "Video");
			}
		}

		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doDraftPost(UploadPost post) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.FILE_UPLOAD);
		apiRequest.setRequestName(RequestName.DRAFT_POST);
		if (post.getId() == 0) {
			apiRequest.setReqParam("id", "");
		} else {
			apiRequest.setReqParam("id", post.getId() + "");
		}

		apiRequest.setReqParam("headline", post.getHeadLine());
		apiRequest.setReqParam("description", post.getDescription());
		apiRequest.setReqParam("category", post.getCategory() + "");
		apiRequest.setReqParam("impact", post.getImpact() + "");
		apiRequest.setReqParam("hashtags", post.getHashTags());
		apiRequest.setReqParam("userType", post.getUserType());
		apiRequest.setReqParam("position", post.getPositionString());

		// apiRequest.setReqParam("draftId", post.getDraftId()+"");
		apiRequest.setReqParam("source", post.getUserType());// ////////////////////////////////////////

		if (null != post.getFiles() && !post.isVideo()) {
			for (Uri uri : post.getFiles()) {
				apiRequest.setReqParam("postType", "Image");
				apiRequest.addPostFile(new PostFile("images[]", new File(uri.toString())));

			}

		} else if (null != post.getFiles() && post.isVideo()) {
			for (Uri uri : post.getFiles()) {
				apiRequest.addPostFile(new PostFile("video", new File(uri.toString())));
				apiRequest.setReqParam("postType", "Video");
			}
		}

		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse doSaveProfileSettings(User user, List<Uri> mediaUris) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.FILE_UPLOAD);
		apiRequest.setRequestName(RequestName.SET_PROFILE_SETTINGS);

		apiRequest.setReqParam("reporterHandle", user.getUsername());
		apiRequest.setReqParam("firstName", user.getFirstName());
		apiRequest.setReqParam("lastName", user.getLastName());
		apiRequest.setReqParam("gender", user.getGender());
		apiRequest.setReqParam("country", user.getCountry());
		apiRequest.setReqParam("city", user.getCity());
		apiRequest.setReqParam("contactNumber", user.getContactNo());
		apiRequest.setReqParam("about", user.getAbout());

		// apiRequest.setReqParam("draftId", post.getDraftId()+"");
		apiRequest.setReqParam("source", "self");// ////////////////////////////////////////

		if (mediaUris.size() > 0) {
			apiRequest.addPostFile(new PostFile("profilePicture", new File(mediaUris.get(0).toString())));

		}

		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;
	}

	public ApiResponse DoDeletePosts(List<Integer> deleteList) {

		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setReqParam("postIds", CommonUtil.doListIntToString(deleteList));
		apiRequest.setRequestName(RequestName.DO_DELETE_POST);
		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse DoPublish(List<Integer> deleteList) {

		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setReqParam("postIds", CommonUtil.doListIntToString(deleteList));
		apiRequest.setRequestName(RequestName.DO_PUBLISH);
		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}

	public ApiResponse DoUnPublish(List<Integer> deleteList) {

		ApiResponse apiResponse = new ApiResponse();
		ApiRequest apiRequest = new ApiRequest(true);
		apiRequest.setRequestMethod(RequestMethod.POST);
		apiRequest.setReqParam("postIds", CommonUtil.doListIntToString(deleteList));
		apiRequest.setRequestName(RequestName.DO_UNPUBLISH);
		ServerResponse response = null;

		try {
			response = httpHelper.sendRequest(apiRequest, true);
			apiResponse = responseHelper.getApiResponse(response);
		} catch (CommunicationException e) {
			e.printStackTrace();
			return apiResponse.setError(e.getApiError());
		}
		return apiResponse;

	}
}
