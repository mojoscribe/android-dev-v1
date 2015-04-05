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
import com.sudosaints.android.mojoscribe.adapter.PostListAdapter;
import com.sudosaints.android.mojoscribe.adapter.PostListAdapter.FetchNextPagePostsListener;
import com.sudosaints.android.mojoscribe.model.Feed;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class CategoriesActivity extends Activity {

	UIHelper uiHelper;
	PullToRefreshListView feedsListView;
	PostListAdapter feedsAdapter;
	List<Feed> feeds;
	private FetchNextPagePostsListener fetchNextPagePostsListener = new FetchNextPagePostsListener() {

		@Override
		public void fetchNextPagePosts() {
			String idsCsv = "";
			for (Feed feed : feeds) {
				idsCsv += feed.getId() + ",";
			}

			new DoPagination(CategoriesActivity.this, idsCsv, getIntent().getIntExtra(IntentExtras.INTENT_CATEGORIES_ID, 0)).execute();

		}
	};
	private boolean bIsPulled = false;

	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.leftDrawer.toggleMenu();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uiHelper = new UIHelper(CategoriesActivity.this);

		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.CATEGOIES_DISP, R.layout.activity_main);
		feedsListView = (PullToRefreshListView) findViewById(R.id.feedsListView);
		feedsListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new getCategories(CategoriesActivity.this, getIntent().getIntExtra(IntentExtras.INTENT_CATEGORIES_ID, 0)).execute();
				bIsPulled = true;
			}
		});

		new getCategories(CategoriesActivity.this, getIntent().getIntExtra(IntentExtras.INTENT_CATEGORIES_ID, 0)).execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	void displayCategoriesFeed() {

	}

	class getCategories extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private int categoriesId;

		public getCategories(Context context, int categoriesId) {
			super();
			this.context = context;
			this.categoriesId = categoriesId;
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
			ApiResponse apiResponse = apiRequestHelper.doCategoriesFeeds(categoriesId);
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

				Intent intent = new Intent(CategoriesActivity.this, CategoriesListActivity.class);
				startActivity(intent);
				finish();
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}

	class DoPagination extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		String idsCSV;
		private int categoriesId;

		public DoPagination(Context context, String idsCSV, int categoriesId) {
			super();
			this.context = context;
			this.idsCSV = idsCSV;
			this.categoriesId = categoriesId;
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
			ApiResponse apiResponse = apiRequestHelper.doCategoriesFeedsPaginate(categoriesId, idsCSV);
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

}
