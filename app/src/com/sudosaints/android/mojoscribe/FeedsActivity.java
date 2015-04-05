package com.sudosaints.android.mojoscribe;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.widget.AbsListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.PostListAdapter;
import com.sudosaints.android.mojoscribe.adapter.PostListAdapter.FetchNextPagePostsListener;
import com.sudosaints.android.mojoscribe.model.Feed;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class FeedsActivity extends Activity {
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 2001;
	private Context context;
	public static boolean backPress = false;
	private Preferences preferences;
	private Logger logger;
	private PullToRefreshListView feedsListView;
	private PostListAdapter feedsAdapter;
	private UIHelper uiHelper;
	private List<Feed> feeds;
	private GoogleCloudMessaging googleCloudMessaging;
	private boolean bIsPulled = false, isSearch = false;
	private Typeface typeface;
	private FetchNextPagePostsListener fetchNextPagePostsListener = new FetchNextPagePostsListener() {

		@Override
		public void fetchNextPagePosts() {
			// TODO Auto-generated method stub
			String idsCsv = "";
			for (Feed feed : feeds) {
				idsCsv += feed.getId() + ",";
			}
			if (null != getIntent().getStringExtra(IntentExtras.INTENT_SEARCH)) {
				isSearch = true;
				new DoSearchPagination(FeedsActivity.this, idsCsv, getIntent().getStringExtra(IntentExtras.INTENT_SEARCH_QUERY)).execute();
			} else {
				new DoPagination(FeedsActivity.this, idsCsv).execute();
			}
		}
	};

	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.leftDrawer.toggleMenu();
			if (preferences.getUserId() > 0) {
				Intent intent = new Intent(FeedsActivity.this, DashBoardActivity.class);
				startActivity(intent);
				finish();
			}
			backPress = false;
		}
	};
	
	public void setBackFalse(){
		backPress=false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);

		preferences = new Preferences(this);
		logger = new Logger(this);
		uiHelper = new UIHelper(this);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.HOME, R.layout.activity_main);

		feedsListView = (PullToRefreshListView) findViewById(R.id.feedsListView);
		feedsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				backPress=false;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});

		if (preferences.getGcmId().length() == 0 && checkPlayServices()) {
			googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
			new GCMRegTask(this.getApplicationContext()).execute();
		}

		if (null != getIntent().getStringExtra(IntentExtras.INTENT_SEARCH)) {
			if (getIntent().getStringExtra(IntentExtras.INTENT_SEARCH).equalsIgnoreCase(IntentExtras.INTENT_SEARCH)) {
				Map<String, Object> dataMap = DataCache.getDataMap();

				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				feeds = DataMapParser.getFeeds(data);
				logger.debug("stop");
				feedsAdapter = new PostListAdapter(this, feeds, fetchNextPagePostsListener);
				feedsListView.setAdapter(feedsAdapter);
				
				feedsListView.setOnRefreshListener(new OnRefreshListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						feedsListView.onRefreshComplete();
					}
				});

			}
		} else {

			feedsListView.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					new GetLandingFeeds(FeedsActivity.this).execute();
					bIsPulled = true;
				}
			});
			new GetLandingFeeds(FeedsActivity.this).execute();

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
		backPress = false;
	}

	class GetLandingFeeds extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private ProgressDialog progressDialog;

		public GetLandingFeeds(Context context) {
			super();
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
			ApiResponse apiResponse = apiRequestHelper.doMainPageFeeds();
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
				if (bIsPulled) {
					feeds.clear();
					feeds.addAll(DataMapParser.getFeeds(data));
					feedsListView.onRefreshComplete();
					bIsPulled = false;
					feedsAdapter.hasNewPosts=true;
				} else {
					feeds = DataMapParser.getFeeds(data);
					feedsAdapter = new PostListAdapter(this.context, feeds, fetchNextPagePostsListener);
					feedsListView.setAdapter(feedsAdapter);
				}

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");

				setResult(RESULT_CANCELED);
			}
		}
	}

	class DoPagination extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		String idsCSV;

		public DoPagination(Context context, String idsCSV) {
			super();
			this.context = context;
			this.idsCSV = idsCSV;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			//progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doFeedsPagination(idsCSV);
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

				feedsAdapter.setPostsList(DataMapParser.getFeeds(data));
				

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class DoSearchPagination extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		String idsCSV, query;

		public DoSearchPagination(Context context, String idsCSV, String query) {
			super();
			this.context = context;
			this.idsCSV = idsCSV;
			this.query = query;
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
			ApiResponse apiResponse = apiRequestHelper.doSearchPagination(idsCSV, query);
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

				feedsAdapter.setPostsList(DataMapParser.getFeeds(data));

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isSearch) {
			startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
			finish();
		} else {
			if (!backPress) {
				uiHelper.CustomToast("Press again to exit");

				backPress = true;
			} else {
				finish();
			}
		}
	}

	/**
	 * GCM Methods
	 */

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				logger.debug("This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private class GCMRegTask extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;

		/**
		 * @param context
		 */
		public GCMRegTask(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected ApiResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String gcmId;
			try {
				gcmId = googleCloudMessaging.register(context.getResources().getString(R.string.gcm_id));
				logger.debug("Device registered successfully...Device Id is -" + gcmId);
				preferences.setGcmId(gcmId);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}

}
