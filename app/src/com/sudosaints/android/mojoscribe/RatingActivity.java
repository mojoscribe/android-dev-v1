package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class RatingActivity extends Activity {

	private com.sudosaints.android.mojoscribe.view.VerticalSeekBar bar;
	private TextView progressTextView;
	private int rating;
	private float ratingLevel;
	private AlertDialog impactDialog;
	private Logger log;
	private int postId;
	private float ratingPost;
	private Preferences preferences;
	private ImageView actionBarleftImageView, actionBarSecondImageView, actionBarThirdImageView, actionBarFourthImageView;
	private Typeface typeface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		preferences = new Preferences(this);
		if (preferences.getUserId() == 0) {
			Toast.makeText(getApplicationContext(), "Login to rate this post", Toast.LENGTH_LONG).show();
			finish();
		}
		setContentView(R.layout.rating_layout);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		actionBarleftImageView = (ImageView) findViewById(R.id.actionBarLeftImageView);
		actionBarSecondImageView = (ImageView) findViewById(R.id.actionBarSecondImageView);
		actionBarThirdImageView = (ImageView) findViewById(R.id.actionBarThirdImageView);
		actionBarFourthImageView = (ImageView) findViewById(R.id.actionBarFourthImageView);

		actionBarleftImageView.setImageResource(R.drawable.arrow_left);
		actionBarleftImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		actionBarSecondImageView.setVisibility(View.INVISIBLE);
		actionBarThirdImageView.setVisibility(View.INVISIBLE);
		actionBarFourthImageView.setImageResource(R.drawable.search_icon);
		actionBarFourthImageView.setVisibility(View.INVISIBLE);

		log = new Logger(getApplicationContext());
		postId = getIntent().getIntExtra(IntentExtras.INTENT_FEED_ID, 0);
		ratingPost = getIntent().getFloatExtra(IntentExtras.INTENT_POST_RATING, (float) 0.0);

		progressTextView = (TextView) findViewById(R.id.progressTextView);
		bar = (com.sudosaints.android.mojoscribe.view.VerticalSeekBar) findViewById(R.id.seekBar1);
		bar.setProgress((int) ratingPost * 10);
		progressTextView.setText(ratingPost + "");
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				showRatingDialog();
				if (seekBar.getProgress() == 0) {
					seekBar.setProgress(15);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub

				if (!fromUser) {
					log.info(progress + "");

					if (progress == 0) {
						seekBar.setProgress(1);
					}
					rating = progress;
					if (progress > 0 && progress < 15) {
						//seekBar.setProgress(15);
						
						ratingLevel = -3;
					} else if (progress > 15 && progress < 40) {
						//seekBar.setProgress(40);
//						rating = 40;
						ratingLevel = -1;
					} else if (progress > 40 && progress < 65) {
						//seekBar.setProgress(65);
//						rating = 65;
						ratingLevel = 0.5f;
					} else if (progress > 65 && progress < 90) {
						//seekBar.setProgress(90);
//						rating = 90;
						ratingLevel = 1.5f;
					} else if (progress > 90) {
						//seekBar.setProgress(100);
//						rating = 100;
						ratingLevel = 3;
					}
					progressTextView.setText(seekBar.getProgress() / 10f + "");
				}
			}
		});

	}

	void showRatingDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RatingActivity.this);

		// set title
		alertDialogBuilder.setTitle("MojoScribe");

		// set dialog message
		alertDialogBuilder.setMessage("Do you want rate this Post a " + rating / 10f+"?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, close
				// current activity
				new doPostRating(RatingActivity.this, ratingLevel, postId).execute();
				dialog.cancel();
			}
		}).setNegativeButton("Rerate", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	void showImpactDialog(final List<IdValue> impactList) {

		String[] impacts = CommonUtil.getValue(impactList);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select The Impact");
		builder.setSingleChoiceItems(impacts, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				new doPostImpact(RatingActivity.this, impactList.get(item).getId(), postId).execute();

				impactDialog.dismiss();
			}
		});
		impactDialog = builder.create();
		impactDialog.show();
	}

	class doPostRating extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private int postId;
		private float ratingValues;

		public doPostRating(Context context, float ratingValues, int postId) {
			super();
			this.context = context;
			this.ratingValues = ratingValues;
			this.postId = postId;
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
			ApiResponse apiResponse = apiRequestHelper.doPostRating(postId, ratingValues);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				List<Map<String, Object>> impacts = (List<Map<String, Object>>) data.get("impacts");
				List<IdValue> impactList = DataMapParser.getImpacts(impacts);
				showImpactDialog(impactList);
			} else {
				Toast.makeText(context, CommonUtil.doSpannableString(result.getError().getMessage() + "", typeface), Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
			}
		}

	}

	class doPostImpact extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private int impactId, postId;

		public doPostImpact(Context context, int impactId, int postId) {
			super();
			this.context = context;
			this.impactId = impactId;
			this.postId = postId;
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
			ApiResponse apiResponse = apiRequestHelper.doPostImpact(postId, impactId);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				Toast.makeText(RatingActivity.this, "Rated", Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(context, CommonUtil.doSpannableString(result.getError().getMessage() + "", typeface), Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
			}
		}

	}

}