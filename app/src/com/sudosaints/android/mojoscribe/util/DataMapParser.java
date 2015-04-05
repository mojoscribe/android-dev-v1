package com.sudosaints.android.mojoscribe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.R.integer;

import com.sudosaints.android.mojoscribe.adapter.PollAdapter;
import com.sudosaints.android.mojoscribe.model.DashboardFeed;
import com.sudosaints.android.mojoscribe.model.DashboardPreferredPosts;
import com.sudosaints.android.mojoscribe.model.Feed;
import com.sudosaints.android.mojoscribe.model.FeedDraft;
import com.sudosaints.android.mojoscribe.model.FeedFiles;
import com.sudosaints.android.mojoscribe.model.FeedSingle;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.model.NewsRoomPosts;
import com.sudosaints.android.mojoscribe.model.NewsRoomSubscription;
import com.sudosaints.android.mojoscribe.model.Notification;
import com.sudosaints.android.mojoscribe.model.Poll;
import com.sudosaints.android.mojoscribe.model.PollAnswerOption;
import com.sudosaints.android.mojoscribe.model.SettingsCategoriesOption;
import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.model.UploadedFeed;

public class DataMapParser {

	@SuppressWarnings("unchecked")
	public static List<Feed> getFeeds(List<Map<String, Object>> feedMaps) {
		List<Feed> feeds = new ArrayList<Feed>();
		try {
			for (Map<String, Object> fieldMap : feedMaps) {
				Feed feed = new Feed();
				feed.setId(Integer.parseInt(fieldMap.get("id").toString()));
				feed.setAuthorId(Integer.parseInt(fieldMap.get("authorId").toString()));
				feed.setFeedHeadLine((String) fieldMap.get("headline"));
				feed.setAuthorName((String) fieldMap.get("author"));
				feed.setOwnerImageUrl(fieldMap.get("profilePicture") + "");
				feed.setCreationDate((String) fieldMap.get("createdOn"));

				if (fieldMap.get("type").toString().equalsIgnoreCase("Image")) {
					List<Map<String, Object>> listFiles = (List<Map<String, Object>>) fieldMap.get("files");
					List<FeedFiles> feedFiles = new ArrayList<FeedFiles>();
					for (Map<String, Object> files : listFiles) {
						FeedFiles feedFile = new FeedFiles();
						feedFile.setId(Integer.parseInt(files.get("id").toString()));
						feedFile.setFilePath((String) files.get("filePath"));
						feedFiles.add(feedFile);
					}
					feed.setFiles(feedFiles);
					feed.setVideo(false);
				} else {
					List<Map<String, Object>> listFiles = (List<Map<String, Object>>) fieldMap.get("files");
					List<FeedFiles> feedFiles = new ArrayList<FeedFiles>();
					for (Map<String, Object> files : listFiles) {
						FeedFiles feedFile = new FeedFiles();
						feedFile.setId(Integer.parseInt(files.get("id").toString()));
						feedFile.setFilePath((String) files.get("filePath"));
						feedFile.setFileThumb(files.get("thumb").toString());
						feedFiles.add(feedFile);
					}
					feed.setFiles(feedFiles);
					feed.setVideo(true);

				}

				if (null != fieldMap.get("views")) {
					feed.setCountViews((String) (fieldMap.get("views") + ""));
				} else {
					feed.setCountViews("0");
				}
				if (null != fieldMap.get("rating")) {

					feed.setCountRating(Float.parseFloat((String) (fieldMap.get("rating") + "")));
				} else {
					feed.setCountRating(0);
				}

				feed.setcountShares((String) (fieldMap.get("numberOfShares") + ""));
				feed.setImapctType((String) fieldMap.get("impact") + "");

				feeds.add(feed);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feeds;
	}

	@SuppressWarnings("unchecked")
	public static List<Notification> getNotifications(List<Map<String, Object>> notificationsMap) {
		List<Notification> notifications = new ArrayList<Notification>();
		try {
			for (Map<String, Object> map : notificationsMap) {
				Notification notification = new Notification();

				notification.setId(Integer.parseInt(map.get("id").toString()));
				notification.setNotificationText(map.get("notifyText").toString());

				if (null != map.get("image"))
					notification.setImageUrl(map.get("image").toString());

				Map<String, Object> actionMap = (Map<String, Object>) map.get("action");

				if (null != actionMap.get("id")) {
					notification.setActionId(Integer.parseInt(actionMap.get("id").toString()));
				}
				if (null != actionMap.get("type")) {
					notification.setActionTypeString(actionMap.get("type").toString());
					notifications.add(notification);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notifications;
	}

	public static List<FeedDraft> getDraftList(List<Map<String, Object>> mapFeedDraftData) {
		List<FeedDraft> list = new ArrayList<FeedDraft>();
		return list;
	}

	@SuppressWarnings("unchecked")
	public static List<UploadedFeed> getUploadedFeed(List<Map<String, Object>> mapFeedPublished) {
		List<UploadedFeed> list = new ArrayList<UploadedFeed>();
		for (Map<String, Object> feedMap : mapFeedPublished) {
			UploadedFeed uploadedFeed = new UploadedFeed();
			uploadedFeed.setHeadline(feedMap.get("headline").toString() + "");
			uploadedFeed.setId(Integer.parseInt(feedMap.get("id").toString() + ""));
			uploadedFeed.setUpdatedOn(feedMap.get("updatedOn").toString());
			List<IdValue> files = new ArrayList<IdValue>();
			for (Map<String, Object> filesMap : (List<Map<String, Object>>) feedMap.get("files")) {
				IdValue file = new IdValue();
				file.setId(Integer.parseInt(filesMap.get("id").toString() + ""));
				file.setName(filesMap.get("file").toString());
				files.add(file);
			}
			uploadedFeed.setFiles(files);
			list.add(uploadedFeed);
		}
		return list;
	}

	public static List<DashboardFeed> getDashboardFeed(List<Map<String, Object>> mapListData) {
		List<DashboardFeed> dashboardFeedsList = new ArrayList<DashboardFeed>();
		for (Map<String, Object> mapData : mapListData) {
			DashboardFeed dashboardFeed = new DashboardFeed();
			dashboardFeed.setId(Integer.parseInt(mapData.get("id").toString()));
			if (mapData.get("author") != null)
				dashboardFeed.setAuthor(mapData.get("author").toString());
			dashboardFeed.setHeadline(mapData.get("headline").toString());
			dashboardFeed.setRating(mapData.get("rating") + "");
			if (mapData.get("views") != null)
			dashboardFeed.setViews(mapData.get("views").toString() + "");
			List<IdValue> files = new ArrayList<IdValue>();
			for (Map<String, Object> fileMap : (List<Map<String, Object>>) mapData.get("files")) {
				IdValue file = new IdValue();
				file.setId(Integer.parseInt(fileMap.get("id").toString()));
				file.setName(fileMap.get("file").toString());
				files.add(file);
			}
			dashboardFeed.setFiles(files);
			dashboardFeedsList.add(dashboardFeed);
		}

		return dashboardFeedsList;
	}

	public static List<Poll> getPolls(List<Map<String, Object>> mapListData) {
		List<Poll> polls = new ArrayList<Poll>();

		for (Map<String, Object> mapData : mapListData) {
			Poll poll = new Poll();

			poll.setId(Integer.parseInt(mapData.get("id").toString()));
			poll.setQuestion(mapData.get("question").toString());
			poll.setAnswered(Boolean.parseBoolean(mapData.get("isAnswered").toString()));

			List<PollAnswerOption> pollAnswerOptions = new ArrayList<PollAnswerOption>();

			List<Map<String, Object>> poolMapList = (List<Map<String, Object>>) mapData.get("results");

			for (Map<String, Object> pollMap : poolMapList) {
				PollAnswerOption answerOption = new PollAnswerOption();
				answerOption.setId(Integer.parseInt(pollMap.get("id").toString()));
				answerOption.setCount(Integer.parseInt(pollMap.get("count").toString()));
				answerOption.setAnswer(pollMap.get("option").toString());
				answerOption.setPercentage(Float.parseFloat(pollMap.get("percentage").toString()));

				pollAnswerOptions.add(answerOption);
			}

			poll.setAnswers(pollAnswerOptions);
			polls.add(poll);

		}

		return polls;
	}

	public static List<PollAnswerOption> getPoll(Map<String, Object> mapData) {

		List<PollAnswerOption> pollAnswerOptions = new ArrayList<PollAnswerOption>();

		List<Map<String, Object>> poolMapList = (List<Map<String, Object>>) mapData.get("results");

		for (Map<String, Object> pollMap : poolMapList) {
			PollAnswerOption answerOption = new PollAnswerOption();
			answerOption.setId(Integer.parseInt(pollMap.get("id").toString()));
			answerOption.setCount(Integer.parseInt(pollMap.get("count").toString()));
			answerOption.setAnswer(pollMap.get("option").toString());
			answerOption.setPercentage(Float.parseFloat(pollMap.get("percentage").toString()));

			pollAnswerOptions.add(answerOption);
		}

		return pollAnswerOptions;
	}

	@SuppressWarnings("unchecked")
	public static List<DashboardPreferredPosts> getDashboardPreferredFeed(List<Map<String, Object>> dataMapList) {
		List<DashboardPreferredPosts> list = new ArrayList<DashboardPreferredPosts>();
		if (null != dataMapList)
			for (Map<String, Object> dataMap : dataMapList) {
				DashboardPreferredPosts feed = new DashboardPreferredPosts();
				feed.setId(Integer.parseInt(dataMap.get("id").toString()));
				feed.setName(dataMap.get("name").toString());
				feed.setFeeds(DataMapParser.getDashboardFeed((List<Map<String, Object>>) dataMap.get("posts")));
				list.add(feed);
			}
		return list;

	}

	public static User getNewsRoomUserInfo(Map<String, Object> userMap) {
		int id = Integer.parseInt(userMap.get("id").toString());
		String UserName = userMap.get("userName").toString() + "";
		String fullName = userMap.get("fullName").toString() + "";
		String userUrl = "";
		if (null != userMap.get("profilePicture"))
			userUrl = userMap.get("profilePicture").toString() + "";

		String joinDate = userMap.get("joinDate").toString() + "";
		String about = userMap.get("about").toString() + "";
		String gender = userMap.get("gender").toString() + "";
		String location = userMap.get("location").toString() + "";
		boolean isFollower;
		if (null != userMap.get("isFollow")) {
			isFollower = Boolean.parseBoolean(userMap.get("isFollow") + "");
		} else {
			isFollower = true;
		}
		String postUrl = "";

		User user = new User(id, UserName, fullName, userUrl, postUrl, joinDate, about, gender, location, isFollower);
		user.setSubscribers((userMap.get("subscribers").toString()));
		user.setRepoterLevel("0");

		return user;
	}

	public static List<NewsRoomSubscription> getsubscriptions(List<Map<String, Object>> subscriptionsList) {
		List<NewsRoomSubscription> list = new ArrayList<NewsRoomSubscription>();
		for (Map<String, Object> mapData : subscriptionsList) {
			NewsRoomSubscription newsRoomSubscription = new NewsRoomSubscription();
			newsRoomSubscription.setId(Integer.parseInt(mapData.get("followedUserId").toString()));
			newsRoomSubscription.setUsername(mapData.get("followedUserUserName").toString() + "");
			newsRoomSubscription.setImageUrl(mapData.get("followedUserPicture").toString());
			newsRoomSubscription.setSubscribers(mapData.get("subscribers").toString());
			newsRoomSubscription.setRepoterLevel(mapData.get("reporterLevel").toString());

			list.add(newsRoomSubscription);
		}

		return list;
	}

	public static List<NewsRoomPosts> getNewsRoomPosts(List<Map<String, Object>> recentPostsList) {
		List<NewsRoomPosts> list = new ArrayList<NewsRoomPosts>();
		try {
			for (Map<String, Object> mapData : recentPostsList) {
				NewsRoomPosts newsRoomPosts = new NewsRoomPosts();
				newsRoomPosts.setHeadLine(mapData.get("title").toString() + "");
				newsRoomPosts.setId(Integer.parseInt(mapData.get("id").toString()));
				if (null != mapData.get("file")) {
					newsRoomPosts.setMediaUlr(mapData.get("file").toString());
				} else {
					newsRoomPosts.setMediaUlr("");
				}

				newsRoomPosts.setPostDate(mapData.get("date").toString());

				List<Map<String, Object>> listHashTags = (List<Map<String, Object>>) mapData.get("hashtags");
				List<IdValue> hashTagList = new ArrayList<IdValue>();
				for (Map<String, Object> hashTags : listHashTags) {
					IdValue hashTag = new IdValue();
					hashTag.setId(Integer.parseInt(hashTags.get("id").toString()));
					hashTag.setName(hashTags.get("name") + "");

					hashTagList.add(hashTag);
				}

				newsRoomPosts.setHashTags(hashTagList);

				list.add(newsRoomPosts);
			}

		} catch (Exception e) {

		}

		return list;

	}

	public static List<IdValue> getcategories(List<Map<String, Object>> categoriesList) {
		List<IdValue> idValues = new ArrayList<IdValue>();
		try {
			for (Map<String, Object> map : categoriesList) {
				IdValue idValue = new IdValue();
				idValue.setId(Integer.parseInt(map.get("id").toString()));
				idValue.setName((String) map.get("name"));
				idValues.add(idValue);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return idValues;
	}

	public static List<IdValue> getImpacts(List<Map<String, Object>> impactList) {
		List<IdValue> idValues = new ArrayList<IdValue>();
		try {
			for (Map<String, Object> map : impactList) {
				IdValue idValue = new IdValue();
				idValue.setId(Integer.parseInt(map.get("id").toString()));
				idValue.setName((String) map.get("area") + "");
				idValues.add(idValue);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return idValues;
	}

	public static List<IdValue> getHashTags(List<Map<String, Object>> hashTagsList) {
		List<IdValue> idValues = new ArrayList<IdValue>();
		try {
			for (Map<String, Object> map : hashTagsList) {
				IdValue idValue = new IdValue();
				idValue.setId(Integer.parseInt(map.get("id").toString()));
				idValue.setName((String) map.get("hashtag"));
				idValues.add(idValue);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return idValues;
	}

	@SuppressWarnings("unchecked")
	public static FeedSingle getFeed(List<Map<String, Object>> feedMaps) {
		FeedSingle feedSingle = new FeedSingle();
		try {
			for (Map<String, Object> fieldMap : feedMaps) {

				feedSingle.setId(Integer.parseInt(fieldMap.get("id").toString()));
				feedSingle.setFeedHeadLine((String) fieldMap.get("headline"));
				feedSingle.setAuthorName((String) fieldMap.get("author"));
				feedSingle.setAuthorImageUrl(fieldMap.get("profilePicture") + "");
				feedSingle.setCretionDate(fieldMap.get("date").toString());
				feedSingle.setFeedDescription((String) fieldMap.get("description"));
				feedSingle.setPostType((String) fieldMap.get("postType"));
				feedSingle.setImpactType(fieldMap.get("impact").toString() + "");
				feedSingle.setLinkString(fieldMap.get("link").toString() + "");
				if (null != fieldMap.get("date")) {
					feedSingle.setTime(Long.parseLong(fieldMap.get("date").toString() + "000"));
				}
				if (null != fieldMap.get("location")) {
					feedSingle.setLoaction((fieldMap.get("location").toString()));
				}
				if (null != fieldMap.get("authorId")) {
					feedSingle.setAuthorId(Integer.parseInt(fieldMap.get("authorId").toString()));
				}

				List<FeedFiles> feedFiles = new ArrayList<FeedFiles>();
				if ((fieldMap.get("type").toString().equalsIgnoreCase("Image"))) {
					feedSingle.setVideo(false);
					List<Map<String, Object>> listFiles = (List<Map<String, Object>>) fieldMap.get("files");
					for (Map<String, Object> files : listFiles) {
						FeedFiles feedFile = new FeedFiles();
						feedFile.setId(Integer.parseInt(files.get("id").toString()));
						feedFile.setFilePath((String) files.get("file"));
						feedFiles.add(feedFile);
					}
					feedSingle.setFiles(feedFiles);

				} else {
					feedSingle.setVideo(true);
					List<Map<String, Object>> listFiles = (List<Map<String, Object>>) fieldMap.get("files");
					// List<FeedFiles> feedFiles = new ArrayList<FeedFiles>();
					for (Map<String, Object> files : listFiles) {
						FeedFiles feedFile = new FeedFiles();
						feedFile.setId(Integer.parseInt(files.get("id").toString()));
						feedFile.setFilePath((String) files.get("file"));
						feedFile.setFileThumb(files.get("thumb").toString());
						feedFiles.add(feedFile);
					}
					feedSingle.setFiles(feedFiles);
				}

				List<Map<String, Object>> listTags = (List<Map<String, Object>>) fieldMap.get("hashtags");
				List<IdValue> hashTags = new ArrayList<IdValue>();
				for (Map<String, Object> files : listTags) {
					IdValue hashTag = new IdValue();
					hashTag.setId(Integer.parseInt(files.get("id").toString()));
					hashTag.setName((String) files.get("hashtag"));
					hashTags.add(hashTag);
				}
				feedSingle.setHashTags(hashTags);
				feedSingle.setViewsNoString(fieldMap.get("views").toString() + "");

				if (null != fieldMap.get("numberOfShares")) {
					feedSingle.setSharesNoString(fieldMap.get("numberOfShares").toString() + "");
				} else {
					feedSingle.setSharesNoString("0");
				}
				if (null != fieldMap.get("averageRating")) {
					feedSingle.setAvgRating(Float.parseFloat(fieldMap.get("averageRating").toString() + ""));
				} else {
					feedSingle.setAvgRating(0.0f);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedSingle;
	}

	@SuppressWarnings("unchecked")
	public static User getSettingsData(Map<String, Object> data) {
		User user = new User();
		try {
			user.setId(Long.parseLong(data.get("id").toString()));
			user.setUsername(data.get("userName").toString());
			user.setEmail(data.get("email").toString());
			user.setContactNo(data.get("contactNumber").toString());
			user.setFirstName(data.get("firstName").toString());
			user.setLastName(data.get("lastName").toString());
			user.setImageUrl(data.get("picture").toString() + "");
			user.setGender(data.get("gender").toString());
			user.setCountry(data.get("country").toString());
			user.setCity(data.get("city").toString());
			user.setAbout(data.get("about").toString());
			List<SettingsCategoriesOption> categoriesOption = new ArrayList<SettingsCategoriesOption>();

			List<Map<String, Object>> userCategories = (List<Map<String, Object>>) data.get("userCategories");

			for (Map<String, Object> map : userCategories) {
				SettingsCategoriesOption option = new SettingsCategoriesOption();
				option.setId(Integer.parseInt(map.get("id").toString()));
				option.setOptionName(map.get("name").toString());
				option.setSelected(Boolean.parseBoolean(map.get("set").toString() + ""));

				categoriesOption.add(option);
			}
			user.setCategoriesOptions(categoriesOption);

			List<String> locationPrefList = new ArrayList<String>();

			List<Map<String, Object>> locMapData = (List<Map<String, Object>>) data.get("userLocations");

			for (Map<String, Object> map : locMapData) {
				IdValue value = new IdValue();
				value.setId(Integer.parseInt(map.get("id").toString()));
				value.setName(map.get("location").toString());
				locationPrefList.add(value.getName());
			}
			user.setPreferanceLocation(locationPrefList);

			user.setMobileNotification(Boolean.parseBoolean(data.get("mobileNotification").toString() + ""));
			user.setEmailNotification(Boolean.parseBoolean(data.get("emailNotification").toString() + ""));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
