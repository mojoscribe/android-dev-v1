package com.sudosaints.android.mojoscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.google.android.gms.plus.PlusShare;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.db.DBUtils;
import com.sudosaints.android.mojoscribe.model.FeedSingle;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.Constants;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.DateHelper;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class SingleFeedActivity extends Activity {
	private ImageView actionBarFirstImageView, actionBarSecondImageView, actionBarThirdImageView, actionBarFourthImageView;
	private ImageView feedImageView, authorImageView, playImageView;
	private TextView headLineTextView, descriptionTextView, hashTagsTextView;
	private TextView viewsTextView, sharesTextView, impactTypeTextView;
	private TextView avgRatingsTextView, locationNDateTextView;
	private FeedSingle feedSingle;
	private Drawable defaultDrawable;
	private Preferences preferences;
	private DBUtils dbUtils;
	private boolean isPostViewed = false;
	private Typeface typeface;
	private Timer timer;
	private Button shareButton;
	private CountDownTimer countDownTimer = null;
	private boolean isShareFacebook = false;
	private ProgressDialog progressDialog;
	private Logger logger;
	private UiLifecycleHelper fbUiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		preferences = new Preferences(this);
		dbUtils = new DBUtils(this);
		logger = new Logger(SingleFeedActivity.this);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		if (getIntent().hasExtra(IntentExtras.INTENT_ACTIVE_NOTIFICATION))
			preferences.setActiveNotification(false);

		setContentView(R.layout.feed_display);

		fbUiHelper = new UiLifecycleHelper(this, callback);
		fbUiHelper.onCreate(savedInstanceState);

		actionBarFirstImageView = (ImageView) findViewById(R.id.actionBarLeftImageView);
		actionBarFirstImageView.setImageResource(R.drawable.arrow_left);
		shareButton = (Button) findViewById(R.id.singleFeedShareButton);
		feedImageView = (ImageView) findViewById(R.id.singleFeedImageView);
		headLineTextView = (TextView) findViewById(R.id.singleFeedTitleTextView);
		descriptionTextView = (TextView) findViewById(R.id.singleFeedDescriptionTextView);
		hashTagsTextView = (TextView) findViewById(R.id.singleFeedHashTagsTextView);
		actionBarSecondImageView = (ImageView) findViewById(R.id.actionBarSecondImageView);
		actionBarThirdImageView = (ImageView) findViewById(R.id.actionBarThirdImageView);
		actionBarFourthImageView = (ImageView) findViewById(R.id.actionBarFourthImageView);
		viewsTextView = (TextView) findViewById(R.id.singleFeedViewsTextView);
		sharesTextView = (TextView) findViewById(R.id.singleFeedCommentsTextView);
		impactTypeTextView = (TextView) findViewById(R.id.singleFeedImpactTextView);
		avgRatingsTextView = (TextView) findViewById(R.id.singleFeedRatingtextView);
		playImageView = (ImageView) findViewById(R.id.singleFeedPlayImageView);
		authorImageView = (ImageView) findViewById(R.id.singleFeedAuthorImageView1);
		locationNDateTextView = (TextView) findViewById(R.id.singleFeedLocDateTextView);

		actionBarSecondImageView.setVisibility(View.INVISIBLE);
		actionBarThirdImageView.setVisibility(View.INVISIBLE);
		actionBarFourthImageView.setVisibility(View.INVISIBLE);
		defaultDrawable = this.getResources().getDrawable(R.drawable.background);

		actionBarFirstImageView.setOnClickListener(new OnClickListener() {
			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SingleFeedActivity.this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
				finish();

			}
		});
		if (dbUtils.getDBHelper().isDbOpened()) {
			isPostViewed = dbUtils.getDBHelper().isPostId(getIntent().getIntExtra(IntentExtras.INTENT_FEED_ID, 0));
			if (!isPostViewed) {
				dbUtils.getDBHelper().savePostId(getIntent().getIntExtra(IntentExtras.INTENT_FEED_ID, 0));
			}
		}
		new GetSingleFeed(SingleFeedActivity.this, getIntent().getIntExtra(IntentExtras.INTENT_FEED_ID, 0), isPostViewed).execute();

		headLineTextView.setTypeface(typeface);
		descriptionTextView.setTypeface(typeface);
		hashTagsTextView.setTypeface(typeface);
		viewsTextView.setTypeface(typeface);
		sharesTextView.setTypeface(typeface);
		impactTypeTextView.setTypeface(typeface);
		avgRatingsTextView.setTypeface(typeface);
		locationNDateTextView.setTypeface(typeface);

		shareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(SingleFeedActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.share_layout);
				dialog.setCancelable(true);

				RelativeLayout fbLayout = (RelativeLayout) dialog.findViewById(R.id.shareViaFacebookRelativeLayout);
				RelativeLayout gpLayout = (RelativeLayout) dialog.findViewById(R.id.shareGPlusLoginRelativeLayout6);
				RelativeLayout twLayout = (RelativeLayout) dialog.findViewById(R.id.shareTwitterLoginRelativeLayout6);

				fbLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						isShareFacebook = true;
						Session session = Session.getActiveSession();
						if (session != null && session.isOpened()) {
							logger.debug("Session opened");
							postToFb();
						} else {
							logger.debug("Session not opened");
						}
						dialog.dismiss();
					}
				});

				twLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SingleFeedActivity.this, TwitterOAuthActivity.class);
						intent.putExtra(IntentExtras.INTENT_LINK, feedSingle.getFeedHeadLine() + "\n" + feedSingle.getLinkString());
						startActivityForResult(intent, Constants.REQUEST_SHARE_ON_TWITTER);
						dialog.dismiss();
					}
				});

				gpLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Launch the Google+ share dialog with attribution to
						// your app.
						Intent shareIntent = new PlusShare.Builder(SingleFeedActivity.this).setType("text/plain").setText("Sharing news").setContentUrl(Uri.parse(feedSingle.getLinkString())).getIntent();
						startActivityForResult(shareIntent, Constants.REQUEST_SHARE_ON_GPLUS);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}

	class GetSingleFeed extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		int id;
		private ProgressDialog progressDialog;
		private boolean isPostViewed;

		public GetSingleFeed(Context context, int id, boolean isPostViewed) {
			super();
			this.id = id;
			this.context = context;
			this.isPostViewed = isPostViewed;
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
			ApiResponse apiResponse = apiRequestHelper.doFeedSingle(id, isPostViewed);
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
				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				feedSingle = DataMapParser.getFeed(data);

				if (null != feedSingle.getFiles() && feedSingle.getFiles().size() > 1) {

					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						int counter = 0;

						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								public void run() {
									UrlImageViewHelper.setUseBitmapScaling(false);
									UrlImageViewHelper.setUrlDrawable(feedImageView, feedSingle.getFiles().get(counter).getFilePath() + "", R.drawable.mojoloading);
								}
							});
							int i = counter;
							if (counter >= (feedSingle.getFiles().size() - 1)) {
								counter = 0;
							} else {
								counter++;
							}
						}
					}, 0, com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC * 1000);

				} else {
					if (feedSingle.isVideo()) {
						UrlImageViewHelper.setUrlDrawable(feedImageView, feedSingle.getFiles().get(0).getFileThumb(), defaultDrawable);
						playImageView.setVisibility(View.VISIBLE);
						playImageView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent tostart = new Intent(Intent.ACTION_VIEW);
								tostart.setDataAndType(Uri.parse(feedSingle.getFiles().get(0).getFilePath()), "video/*");
								startActivity(tostart);
							}
						});
						feedImageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent tostart = new Intent(Intent.ACTION_VIEW);
								tostart.setDataAndType(Uri.parse(feedSingle.getFiles().get(0).getFilePath()), "video/*");
								startActivity(tostart);
							}
						});
					} else {
						UrlImageViewHelper.setUrlDrawable(feedImageView, feedSingle.getFiles().get(0).getFilePath(), defaultDrawable);
					}
				}
				if (feedSingle.getAuthorName().equalsIgnoreCase("anonymous")) {
					authorImageView.setVisibility(View.INVISIBLE);
				} else {

					UrlImageViewHelper.setUrlDrawable(authorImageView, feedSingle.getAuthorImageUrl(), R.drawable.profile);
					authorImageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(SingleFeedActivity.this, NewsRoomActivity.class);
							intent.putExtra(IntentExtras.INTENT_USER_ID, feedSingle.getAuthorId());
							startActivity(intent);

						}
					});

				}
				headLineTextView.setText(feedSingle.getFeedHeadLine());
				descriptionTextView.setText(feedSingle.getFeedDescription());
				for (IdValue hashTag : feedSingle.getHashTags()) {
					hashTagsTextView.append("#" + hashTag.getName() + " ");
				}

				Date dateUTC = DateHelper.getUTCDateWithZoneFormat(feedSingle.getTime(), TimeZone.getDefault());
				locationNDateTextView.setText(feedSingle.getLoaction() + " - " + DateHelper.getMMMDDYYYYFormattedDate(dateUTC));

				viewsTextView.setText(feedSingle.getViewsNoString() + " Views");
				sharesTextView.setText(feedSingle.getSharesNoString() + " Shares");
				impactTypeTextView.setText(feedSingle.getImpactType() + " Impact");
				avgRatingsTextView.setText(CommonUtil.getDecimalFormat(feedSingle.getAvgRating()) + "");
				avgRatingsTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent ratingIntent = new Intent(SingleFeedActivity.this, RatingActivity.class);
						ratingIntent.putExtra(IntentExtras.INTENT_FEED_ID, getIntent().getIntExtra(IntentExtras.INTENT_FEED_ID, 0));
						ratingIntent.putExtra(IntentExtras.INTENT_POST_RATING, Float.parseFloat(avgRatingsTextView.getText().toString()));
						startActivity(ratingIntent);
					}
				});
			} else {
				Toast.makeText(context, CommonUtil.doSpannableString(result.getError().getMessage() + "", typeface), Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		dbUtils.getDBHelper().close();
		fbUiHelper.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		fbUiHelper.onPause();
		if (null != countDownTimer) {
			countDownTimer.cancel();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fbUiHelper.onResume();
		if (null != countDownTimer) {
			countDownTimer.start();
		}
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (isShareFacebook)
			postToFb();
	}

	private void postToFb() {
		Session session = Session.getActiveSession();
		if (hasPublishPermission()) {
			// logger.debug("has publish permission");
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Posting to facebook...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			Request request = Request.newStatusUpdateRequest(Session.getActiveSession(), "Mojo-Scribe", new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					showPublishResult("Result", response.getGraphObject(), response.getError());
				}
			});
			Bundle params = request.getParameters();
			params.putString("message", "");
			params.putString("caption", feedSingle.getAuthorName());
			params.putString("description", feedSingle.getFeedDescription());
			params.putString("link", feedSingle.getLinkString());
			params.putString("name", feedSingle.getFeedHeadLine());
			params.putString("picture", feedSingle.getFiles().get(0).getFilePath());

			request.setParameters(params);

			request.executeAsync();
			isShareFacebook = false;
			new DoShareFeed(SingleFeedActivity.this, feedSingle.getId()).execute();
		} else if (session.isOpened()) {
			// logger.debug("dont have publish permission");
			session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, "publish_actions"));
			return;
		}
	}

	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().contains("publish_actions");
	}

	private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
		if (null != progressDialog && progressDialog.isShowing())
			progressDialog.dismiss();

		if (error == null) {
			Toast.makeText(this, "Posted on Facebook!!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Constants.REQUEST_SHARE_ON_TWITTER:
			if (RESULT_OK == resultCode) {
				Toast.makeText(this, "Tweet shared successfully!!", Toast.LENGTH_SHORT).show();
				new DoShareFeed(SingleFeedActivity.this, feedSingle.getId()).execute();
			} else {
				new DoShareFeed(SingleFeedActivity.this, feedSingle.getId()).execute();
				String message = "Unable to share on Twitter!!";
				if (null != data)
					if (data.hasExtra(IntentExtras.TWITTER_RESULT_EXTRA)) {
						message = data.getStringExtra(IntentExtras.TWITTER_RESULT_EXTRA);
					}
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			}
			break;
		case Constants.REQUEST_SHARE_ON_GPLUS:
			if (RESULT_OK == resultCode) {
				Toast.makeText(this, "G+ shared successfully!!", Toast.LENGTH_SHORT).show();
				new DoShareFeed(SingleFeedActivity.this, feedSingle.getId()).execute();
			} else {
				Toast.makeText(this, "Unable to share on G+!!", Toast.LENGTH_SHORT).show();
			}
		default:
			fbUiHelper.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fbUiHelper.onDestroy();
	}

	class DoShareFeed extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		int id;
		private ProgressDialog progressDialog;

		public DoShareFeed(Context context, int id) {
			super();
			this.id = id;
			this.context = context;
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
			ApiResponse apiResponse = apiRequestHelper.doShareSingle(id);
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
				feedSingle.setSharesNoString((Integer.parseInt(feedSingle.getSharesNoString()) + 1) + "");
				sharesTextView.setText(feedSingle.getSharesNoString() + " Shares");

			} else {
				Toast.makeText(context, CommonUtil.doSpannableString(result.getError().getMessage() + "", typeface), Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// startActivity(new Intent(this,
		// FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
		// Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}

}
