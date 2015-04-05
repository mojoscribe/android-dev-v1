package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.NewsRoomFeedListAdapter;
import com.sudosaints.android.mojoscribe.adapter.NewsRoomSubscriberListAdapter;
import com.sudosaints.android.mojoscribe.model.NewsRoomPosts;
import com.sudosaints.android.mojoscribe.model.NewsRoomSubscription;
import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.CustomImageView;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class NewsRoomActivity extends Activity {
	private TabHost tabHost = null;
	private com.sudosaints.android.mojoscribe.view.CustomGridView newsRoomGridView;
	private ImageView newsRoomImageView;
	private TextView newsRoomAuthorNameTextView, newsRoomAuthorLocationTextView, newsRoomAuthorAboutTextView, newsRoomAuthorDescriptionTextView;
	private TextView newsRoomAuthorRepoterLevelTextView, newsRoomAuthorSubscriptionsTextView, newsRoomAuthorJoinDateTextView;
	private Drawable defaultDrawable;
	private Button newsRoomsubscribeButton;
	private int userId;
	private Preferences preferences;
	private ScrollView newsRoomScrollView;
	private User user;
	private List<NewsRoomPosts> recentPosts, ratedPosts;
	private List<NewsRoomSubscription> subscriptions;
	private UIHelper uiHelper;
	private DrawerOptionAction drawerOptionAction;
	private NewsRoomFeedListAdapter newsRoomFeedListAdapter;
	private NewsRoomSubscriberListAdapter newsRoomSubscriberListAdapter;
	private Typeface typeface;
	private com.sudosaints.android.mojoscribe.view.CustomImageView newsRoomAuthorImageView;
	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {

		}
	};

	private void scrollToUp() {
		newsRoomScrollView.post(new Runnable() {
			@Override
			public void run() {
				// newsRoomScrollView.fullScroll(ScrollView.FOCUS_UP);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		preferences = new Preferences(this);
		if (getIntent().hasExtra(IntentExtras.INTENT_ACTIVE_NOTIFICATION))
			preferences.setActiveNotification(false);
		if (getIntent().hasExtra(IntentExtras.INTENT_USER_ID)) {
			userId = getIntent().getIntExtra(IntentExtras.INTENT_USER_ID, 0);

		} else {
			userId = Integer.parseInt(preferences.getUserId() + "");
		}

		setContentView(R.layout.newsroom_layout);
		new GetNewsRoomData(this, userId).execute();
		uiHelper = new UIHelper(this);
		uiHelper.generateDrawers(onOptionSelected, drawerOptionAction.NEWSROOM, R.layout.newsroom_layout);
		defaultDrawable = getResources().getDrawable(R.drawable.background);
		newsRoomGridView = (com.sudosaints.android.mojoscribe.view.CustomGridView) findViewById(R.id.newsRoomGridView);
		newsRoomScrollView = (ScrollView) findViewById(R.id.newsRoomScroller);
		newsRoomImageView = (ImageView) findViewById(R.id.newsRoomRecentPostsImageView);
		newsRoomAuthorImageView = (com.sudosaints.android.mojoscribe.view.CustomImageView) findViewById(R.id.newsRoomAuthorImageView);
		newsRoomAuthorNameTextView = (TextView) findViewById(R.id.newsRoomAuthorFullNameTextView);
		newsRoomAuthorLocationTextView = (TextView) findViewById(R.id.newsRoomAuthorLocationTextView);
		newsRoomAuthorAboutTextView = (TextView) findViewById(R.id.newsRoomAboutAuthorNameTextView);
		newsRoomAuthorDescriptionTextView = (TextView) findViewById(R.id.newsRoomAboutAuthorTextView);
		newsRoomAuthorRepoterLevelTextView = (TextView) findViewById(R.id.newsRoomRepoterLevelTextView);
		newsRoomAuthorSubscriptionsTextView = (TextView) findViewById(R.id.newsRoomSubscribersTextView);
		newsRoomAuthorJoinDateTextView = (TextView) findViewById(R.id.newsRoomMemberSinceTextView);
		newsRoomsubscribeButton = (Button) findViewById(R.id.newsRoomAuthorSubscribeButton);
		tabHost = (TabHost) findViewById(R.id.newsRoomTabHost);
		tabHost.setup();
		newsRoomAuthorNameTextView.setTypeface(typeface);
		newsRoomAuthorLocationTextView.setTypeface(typeface);
		newsRoomAuthorAboutTextView.setTypeface(typeface);
		newsRoomAuthorDescriptionTextView.setTypeface(typeface);
		newsRoomAuthorRepoterLevelTextView.setTypeface(typeface);
		newsRoomAuthorSubscriptionsTextView.setTypeface(typeface);
		newsRoomAuthorJoinDateTextView.setTypeface(typeface);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Map Tab
		TabSpec mapTabSpec1 = tabHost.newTabSpec("1");
		Button tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Recent Posts");
		tabButton.setTypeface(typeface);
		mapTabSpec1.setIndicator(tabButton);
		mapTabSpec1.setContent(R.id.newsRoomGridView);

		// Map Tab
		TabSpec mapTabSpec2 = tabHost.newTabSpec("2");
		tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Rated Posts");
		tabButton.setTypeface(typeface);
		mapTabSpec2.setIndicator(tabButton);
		mapTabSpec2.setContent(R.id.newsRoomGridView);

		// Map Tab
		TabSpec mapTabSpec3 = tabHost.newTabSpec("3");
		tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Subscriptions");
		tabButton.setTypeface(typeface);
		mapTabSpec3.setIndicator(tabButton);
		mapTabSpec3.setContent(R.id.newsRoomGridView);

		tabHost.addTab(mapTabSpec1);
		tabHost.addTab(mapTabSpec2);
		tabHost.addTab(mapTabSpec3);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				newsRoomGridDisplay();
			}
		});

		newsRoomsubscribeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new doSubscribe(NewsRoomActivity.this, userId).execute();
			}
		});
		// scrollToUp();
	}

	void newsRoomDisplayUserInfo() {
		long timerValue = 0;
		if (null != user) {
			if (recentPosts.size() > 0) {
				timerValue = recentPosts.size() * 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC;
				CountDownTimer countDownTimer = new CountDownTimer(timerValue, 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC) {
					int counter = 0;

					@Override
					public void onTick(long millisUntilFinished) {
						UrlImageViewHelper.setUrlDrawable(newsRoomImageView, recentPosts.get(counter).getMediaUlr() + "", defaultDrawable);
						counter++;
					}

					@Override
					public void onFinish() {
						counter = 0;
						this.start();
					}
				};
				countDownTimer.start();
			}
			UrlImageViewHelper.setUrlDrawable(newsRoomAuthorImageView, user.getImageUrl(), R.drawable.profile);
			if (user.getFirstName().length() > 1) {
				newsRoomAuthorNameTextView.setText(user.getFirstName());
			} else {
				newsRoomAuthorNameTextView.setText(user.getUsername());
			}
			newsRoomAuthorLocationTextView.setText(user.getLocation());
			newsRoomAuthorAboutTextView.setText("About " + newsRoomAuthorNameTextView.getText().toString());
			newsRoomAuthorDescriptionTextView.setText(user.getAbout());

			newsRoomAuthorRepoterLevelTextView.setText("Repoter Level - " + user.getRepoterLevel());
			newsRoomAuthorSubscriptionsTextView.setText("Subscribers - " + user.getSubscribers());
			newsRoomAuthorJoinDateTextView.setText("Member Since - " + user.getJoinDate());

			if (user.getId() == preferences.getUserId() || preferences.getUserId() == 0) {
				newsRoomsubscribeButton.setVisibility(View.INVISIBLE);
			} else {
				newsRoomsubscribeButton.setVisibility(View.VISIBLE);
				if (user.isFollowed()) {
					newsRoomsubscribeButton.setText("Following ");
					newsRoomsubscribeButton.setCompoundDrawables(null, null, null, null);
				} else {
					Drawable img = getResources().getDrawable(R.drawable.circular_plus);
					img.setBounds(0, 0, 30, 30);
					newsRoomsubscribeButton.setCompoundDrawables(img, null, null, null);

					newsRoomsubscribeButton.setText(" Subscribe ");
				}
			}

			/*
			 * newsRoomAuthorRepoterLevelTextView; //no data from server yet
			 * newsRoomAuthorSubscriptionsTextView = (TextView)
			 * findViewById(R.id.newsRoomSubscribersTextView);
			 * newsRoomAuthorJoinDateTextView = (TextView)
			 * findViewById(R.id.newsRoomMemberSinceTextView);
			 */

			newsRoomGridDisplay();
		}
	}

	void newsRoomGridDisplay() {
		switch (Integer.parseInt(tabHost.getCurrentTabTag())) {
		case 1:
			if (null != recentPosts) {
				newsRoomFeedListAdapter = new NewsRoomFeedListAdapter(this, recentPosts);
				newsRoomGridView.setAdapter(newsRoomFeedListAdapter);
			}
			break;
		case 2:
			if (null != ratedPosts) {
				newsRoomFeedListAdapter = new NewsRoomFeedListAdapter(this, ratedPosts);
				newsRoomGridView.setAdapter(newsRoomFeedListAdapter);
			}
			break;
		case 3:
			if (null != subscriptions) {
				newsRoomSubscriberListAdapter = new NewsRoomSubscriberListAdapter(this, subscriptions);
				newsRoomGridView.setAdapter(newsRoomSubscriberListAdapter);
			}
			break;
		default:
			// scrollToUp();
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	class GetNewsRoomData extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private int UserId;

		public GetNewsRoomData(Context context, int UserId) {
			super();
			this.context = context;
			this.UserId = UserId;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getNewsRoomData(this.UserId);
			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");

				Map<String, Object> userData = (Map<String, Object>) data.get("user");

				user = DataMapParser.getNewsRoomUserInfo(userData);

				List<Map<String, Object>> recentPostsMap = (List<Map<String, Object>>) data.get("recentPosts");
				recentPosts = DataMapParser.getNewsRoomPosts(recentPostsMap);

				List<Map<String, Object>> ratedPostsMap = (List<Map<String, Object>>) data.get("ratedPosts");
				ratedPosts = DataMapParser.getNewsRoomPosts(ratedPostsMap);

				List<Map<String, Object>> subscriptionsMap = (List<Map<String, Object>>) data.get("subscriptions");
				subscriptions = DataMapParser.getsubscriptions(subscriptionsMap);

				newsRoomDisplayUserInfo();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class doSubscribe extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private int UserId;

		public doSubscribe(Context context, int UserId) {
			super();
			this.context = context;
			this.UserId = UserId;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doSubscribe(this.UserId);
			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				user.setFollowed(!user.isFollowed());
				if (user.isFollowed()) {
					newsRoomsubscribeButton.setText("Following ");
					user.setSubscribers((Integer.parseInt(user.getSubscribers()) + 1) + "");
					newsRoomAuthorSubscriptionsTextView.setText("Subscribers - " + user.getSubscribers());
					newsRoomsubscribeButton.setCompoundDrawables(null, null, null, null);
				} else {
					Drawable img = getResources().getDrawable(R.drawable.circular_plus);
					img.setBounds(0, 0, 30, 30);
					newsRoomsubscribeButton.setCompoundDrawables(img, null, null, null);
					user.setSubscribers((Integer.parseInt(user.getSubscribers()) - 1) + "");
					newsRoomAuthorSubscriptionsTextView.setText("Subscribers - " + user.getSubscribers());
					newsRoomsubscribeButton.setText(" Subscribe ");
				}

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	/*@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}*/

}
