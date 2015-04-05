package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.FeedHorizantalListAdapter;
import com.sudosaints.android.mojoscribe.model.DashboardFeed;
import com.sudosaints.android.mojoscribe.model.DashboardPreferredPosts;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.Constants;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class DashBoardActivity extends Activity {
	private ImageView dasahBoardImageView;
	private TextView dashboardRatingTextView, dashboardHeadlineTextView;
	private ImageButton watchLaterButton;
	private ViewGroup horizantalListViewGroup;
	private LayoutInflater inflater;
	private UIHelper uiHelper;
	private List<DashboardFeed> featuredList;
	private List<DashboardPreferredPosts> dashboardPreferredPosts;
	private Drawable defaultDrawable;
	private Typeface typeface;
	private FeedHorizantalListAdapter feedHorizantalListAdapter;
	private Timer timer;
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
		setContentView(R.layout.dashboard_layout);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		uiHelper = new UIHelper(this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.HOME, R.layout.dashboard_layout);

		defaultDrawable = getResources().getDrawable(R.drawable.background);

		dasahBoardImageView = (ImageView) findViewById(R.id.dasahBoardImageView);
		dashboardHeadlineTextView = (TextView) findViewById(R.id.dashboardHeadlineTextView);
		dashboardRatingTextView = (TextView) findViewById(R.id.dashboardRatingTextView);
		watchLaterButton = (ImageButton) findViewById(R.id.dashboardWatchLaterImageButton);

		new GetDashboardFeeds(this).execute();
		dashboardRatingTextView.setTypeface(typeface);
		dashboardHeadlineTextView.setTypeface(typeface);
	}

	void displayFeatured() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			int count = -1;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				count++;
				if (featuredList == null || featuredList.size() == 0)
					return;
				if (count >= featuredList.size()) {
					count = 0;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						UrlImageViewHelper.setUrlDrawable(dasahBoardImageView, featuredList.get(count).getFiles().get(0).getName() + "", R.drawable.mojoloading);
						dashboardHeadlineTextView.setText(featuredList.get(count).getHeadline() + "");
						dashboardRatingTextView.setText(CommonUtil.getDecimalFormat(Float.parseFloat(featuredList.get(count).getRating())) + "");
						dasahBoardImageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(DashBoardActivity.this, SingleFeedActivity.class);
								intent.putExtra(IntentExtras.INTENT_FEED_ID, featuredList.get(count).getId());
								startActivity(intent);
							}
						});

					}
				});

			}
		}, 0, Constants.MEDIA_FILE_ROTATE_TIME_SEC * 1000);

	}

	void displayPrefeared() {

		horizantalListViewGroup = (ViewGroup) findViewById(R.id.dashboardVirticalLinearLayout);
		FeedHorizantalListAdapter feedHorizantalListAdapter;

		for (final DashboardPreferredPosts post : dashboardPreferredPosts) {
			inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.post_layout_horizantal_scroll, null);

			TextView textView = (TextView) view.findViewById(R.id.horizantalFeedListCategoryTextView);
			ImageButton imageButton = (ImageButton) view.findViewById(R.id.horizantalFeedListActionImageButton);
			com.meetme.android.horizontallistview.HorizontalListView horizantalListView = (com.meetme.android.horizontallistview.HorizontalListView) view.findViewById(R.id.feedHorizantalViewList);

			textView.setText(post.getName() + "");

			feedHorizantalListAdapter = new FeedHorizantalListAdapter(DashBoardActivity.this, post.getFeeds());
			horizantalListView.setAdapter(feedHorizantalListAdapter);
			horizantalListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(DashBoardActivity.this, SingleFeedActivity.class);
					intent.putExtra(IntentExtras.INTENT_FEED_ID, post.getFeeds().get(arg2).getId());
					startActivity(intent);
				}
			});

			horizantalListViewGroup.addView(view);

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	class GetDashboardFeeds extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetDashboardFeeds(Context context) {
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
			ApiResponse apiResponse = apiRequestHelper.getDashboardData();
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
				List<Map<String, Object>> featuredMapList = (List<Map<String, Object>>) data.get("featured");
				List<Map<String, Object>> preferredMapList = (List<Map<String, Object>>) data.get("preferredPosts");
				featuredList = DataMapParser.getDashboardFeed(featuredMapList);
				dashboardPreferredPosts = DataMapParser.getDashboardPreferredFeed(preferredMapList);
				displayFeatured();
				displayPrefeared();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				context.startActivity(new Intent(context, FeedsActivity.class));
				finish();
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
