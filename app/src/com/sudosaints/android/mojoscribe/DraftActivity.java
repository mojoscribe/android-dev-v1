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

import com.sudosaints.android.mojoscribe.adapter.DraftListAdapter;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.model.UploadedFeed;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class DraftActivity extends Activity {

	private GridView draftFeedGridView;
	private List<UploadedFeed> draftFeed;
	private Button deleteDraftButton, publishDraftButton;
	private List<Integer> checkedFeedList;
	private UIHelper uiHelper;
	private DraftListAdapter draftListAdapter;
	private Typeface typeface;
	private TextView noDraftTextView;
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
		setContentView(R.layout.draft_layout);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		uiHelper = new UIHelper(this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.DRAFTS, R.layout.draft_layout);

		checkedFeedList = new ArrayList<Integer>();

		draftFeedGridView = (GridView) findViewById(R.id.draftFeedGridView);
		deleteDraftButton = (Button) findViewById(R.id.draftDeleteButton);
		publishDraftButton = (Button) findViewById(R.id.draftPublishButton);
		deleteDraftButton.setTypeface(typeface);
		publishDraftButton.setTypeface(typeface);

		new GetDraftFeeds(this).execute();

		deleteDraftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkedFeedList = new ArrayList<Integer>();
				for (UploadedFeed row : draftFeed) {
					if (row.isChecked()) {
						checkedFeedList.add(row.getId());
					}

				}
				new DoDeletePublished(DraftActivity.this, checkedFeedList).execute();
			}

		});
		publishDraftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkedFeedList = new ArrayList<Integer>();
				for (UploadedFeed row : draftFeed) {
					if (row.isChecked()) {
						checkedFeedList.add(row.getId());
					}

				}
				new DoPublished(DraftActivity.this, checkedFeedList).execute();

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	class GetDraftFeeds extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetDraftFeeds(Context context) {
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
			ApiResponse apiResponse = apiRequestHelper.getDraftFeedData();
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
				draftFeed = DataMapParser.getUploadedFeed(data);

				draftListAdapter = new DraftListAdapter(context, draftFeed);
				draftFeedGridView.setAdapter(draftListAdapter);
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
				noDraftTextView = (TextView) findViewById(R.id.draftListNoList);
				noDraftTextView.setText("No post to display");
			}
		}

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
				for (UploadedFeed published : draftFeed) {
					if (!published.isChecked()) {
						tempList.add(published);
					}
					draftFeed = tempList;
					draftListAdapter = new DraftListAdapter(context, draftFeed);
					draftFeedGridView.setAdapter(draftListAdapter);

				}
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class DoPublished extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private List<Integer> deleteList;
		private ProgressDialog progressDialog;

		public DoPublished(Context context, List<Integer> deleteList) {
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
			ApiResponse apiResponse = apiRequestHelper.DoPublish(deleteList);
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
				for (UploadedFeed published : draftFeed) {
					if (!published.isChecked()) {
						tempList.add(published);
					}
					draftFeed = tempList;
					draftListAdapter = new DraftListAdapter(context, draftFeed);
					draftFeedGridView.setAdapter(draftListAdapter);

				}
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
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
