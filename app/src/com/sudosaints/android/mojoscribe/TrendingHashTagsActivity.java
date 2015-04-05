package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.PostListAdapter.FetchNextPagePostsListener;
import com.sudosaints.android.mojoscribe.adapter.PostListAdapter;
import com.sudosaints.android.mojoscribe.model.Feed;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class TrendingHashTagsActivity extends Activity {

	private UIHelper uiHelper;
	private ListView feedsListView;
	private PostListAdapter feedsAdapter;
	private List<Feed> feeds;
	private FetchNextPagePostsListener fetchNextPagePostsListener = new FetchNextPagePostsListener() {

		@Override
		public void fetchNextPagePosts() {
			// TODO Auto-generated method stub

		}
	};

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

		uiHelper = new UIHelper(TrendingHashTagsActivity.this);

		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.TRENDING_HASH_TAGS_DISP, R.layout.activity_main);
		feedsListView = (ListView) findViewById(R.id.feedsListView);

		new getCategories(TrendingHashTagsActivity.this, getIntent().getStringExtra(IntentExtras.INTENT_HASHTAG_TAG)).execute();

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
		private String hashTag;

		public getCategories(Context context, String hashTag) {
			super();
			this.context = context;
			this.hashTag = hashTag;
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
			ApiResponse apiResponse = apiRequestHelper.doTrendingHashTagsFeeds(hashTag);
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
				feeds = DataMapParser.getFeeds(data);

				feedsAdapter = new PostListAdapter(this.context, feeds, fetchNextPagePostsListener);
				feedsListView.setAdapter(feedsAdapter);
			} else {
				uiHelper.CustomToast( result.getError().getMessage()+"");
				setResult(RESULT_CANCELED);
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}
}
