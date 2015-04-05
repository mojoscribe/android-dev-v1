package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.PostAnonymousListAdapter;
import com.sudosaints.android.mojoscribe.adapter.PostAnonymousListAdapter.FetchNextPagePostsListener;
import com.sudosaints.android.mojoscribe.model.Feed;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class AnonymousFeedActivity extends Activity {

	private Preferences preferences;
	private Logger logger;
	private PullToRefreshListView feedsListView;
	private PostAnonymousListAdapter feedsAnonymousAdapter;
	private UIHelper uiHelper;
	private boolean bIsPulled = false;
	private List<Feed> feeds;
	private DrawerOptionAction drawerOptionAction;
	private FetchNextPagePostsListener fetchNextPagePostsListener = new FetchNextPagePostsListener() {

		@Override
		public void fetchNextPagePosts() {
			// TODO Auto-generated method stub
			String idsCsv = "";
			for (Feed feed : feeds) {
				idsCsv += feed.getId() + ",";
			}

			new DoPagination(AnonymousFeedActivity.this, idsCsv).execute();

		}
	};

	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.rightDrawer.toggleMenu();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = new Preferences(this);
		logger = new Logger(this);
		uiHelper = new UIHelper(this);
		uiHelper.generateDrawers(onOptionSelected, drawerOptionAction.ANONYMOUS, R.layout.activity_main);

		feedsListView = (PullToRefreshListView) findViewById(R.id.feedsListView);
		feedsListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new GetAnonymousFeeds(AnonymousFeedActivity.this).execute();
				bIsPulled = true;
			}
		});

		new GetAnonymousFeeds(AnonymousFeedActivity.this).execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	class GetAnonymousFeeds extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetAnonymousFeeds(Context context) {
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
			ApiResponse apiResponse = apiRequestHelper.doAnonymousFeeds();
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
					feedsAnonymousAdapter.hasNewPosts=true;
				} else {
					feeds = DataMapParser.getFeeds(data);
					feedsAnonymousAdapter = new PostAnonymousListAdapter(this.context, feeds, fetchNextPagePostsListener);
					feedsListView.setAdapter(feedsAnonymousAdapter);
				}

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stubpackage com.sudosaints.android.mojoscribe;

		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
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
		//	progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doAnonymousPagination(idsCSV);
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

				feedsAnonymousAdapter.setPostsList(DataMapParser.getFeeds(data));

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

}
