package com.sudosaints.android.mojoscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.PublishListAdapter;
import com.sudosaints.android.mojoscribe.model.UploadedFeed;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class PostsListActivity extends Activity {

	private GridView publishFeedGridView;
	private UIHelper uiHelper;
	private Button unpublishButton, deleteButton, sortButton;
	private List<UploadedFeed> uploadedFeeds;
	private PublishListAdapter publishListAdapter;
	private List<Integer> deleteLitId;
	private Typeface typeface;
	private TextView nolistTextView;
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
		setContentView(R.layout.posts_layout);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		deleteLitId = new ArrayList<Integer>();

		uiHelper = new UIHelper(this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.POSTS, R.layout.posts_layout);

		publishFeedGridView = (GridView) findViewById(R.id.publishPostFeedGridView);
		unpublishButton = (Button) findViewById(R.id.publishUnpublishButton);
		deleteButton = (Button) findViewById(R.id.publishDeleteButton);
		sortButton = (Button) findViewById(R.id.publishPostSortButton);
		unpublishButton.setTypeface(typeface);
		deleteButton.setTypeface(typeface);
		sortButton.setTypeface(typeface);

		uploadedFeeds = new ArrayList<UploadedFeed>();
		new GetPublishFeeds(this).execute();

		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteLitId = new ArrayList<Integer>();
				for (UploadedFeed row : uploadedFeeds) {
					if (row.isChecked()) {
						deleteLitId.add(row.getId());
					}

				}
				new DoDeletePublished(PostsListActivity.this, deleteLitId).execute();
			}
		});

		unpublishButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteLitId = new ArrayList<Integer>();
				for (UploadedFeed row : uploadedFeeds) {
					if (row.isChecked()) {
						deleteLitId.add(row.getId());
					}

				}
				new DoFeedUnPublished(PostsListActivity.this, deleteLitId).execute();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	class DoDeletePublished extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private List<Integer> deleteList;
		private ProgressDialog progressDialog;

		public DoDeletePublished(Context context, List<Integer> deleteList) {
			super();
			this.context = context;
			this.deleteList = deleteList;
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
			ApiResponse apiResponse = apiRequestHelper.DoDeletePosts(deleteList);
			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				List<UploadedFeed> tempList = new ArrayList<UploadedFeed>();
				for (UploadedFeed published : uploadedFeeds) {
					if (!published.isChecked()) {
						tempList.add(published);
					}
					uploadedFeeds = tempList;
					publishListAdapter = new PublishListAdapter(context, uploadedFeeds);
					publishFeedGridView.setAdapter(publishListAdapter);

				}
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class DoFeedUnPublished extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private List<Integer> deleteList;
		private ProgressDialog progressDialog;

		public DoFeedUnPublished(Context context, List<Integer> deleteList) {
			super();
			this.context = context;
			this.deleteList = deleteList;
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
			ApiResponse apiResponse = apiRequestHelper.DoUnPublish(deleteList);
			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				List<UploadedFeed> tempList = new ArrayList<UploadedFeed>();
				for (UploadedFeed published : uploadedFeeds) {
					if (!published.isChecked()) {
						tempList.add(published);
					}
					uploadedFeeds = tempList;
					publishListAdapter = new PublishListAdapter(context, uploadedFeeds);
					publishFeedGridView.setAdapter(publishListAdapter);

				}
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class GetPublishFeeds extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetPublishFeeds(Context context) {
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
			ApiResponse apiResponse = apiRequestHelper.getPublishFeedData();
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
				uploadedFeeds = DataMapParser.getUploadedFeed(data);

				publishListAdapter = new PublishListAdapter(context, uploadedFeeds);
				publishFeedGridView.setAdapter(publishListAdapter);
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
				nolistTextView = (TextView) findViewById(R.id.postListNoList);
				nolistTextView.setText("No Post to diaplay");
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}

}
