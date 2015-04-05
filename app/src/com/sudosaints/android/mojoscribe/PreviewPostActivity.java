package com.sudosaints.android.mojoscribe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.model.UploadPost;
import com.sudosaints.android.mojoscribe.util.DataHelper;

public class PreviewPostActivity extends Activity {

	private ImageView previrePostsImageView,actionBarFirstImageView,actionBarSecondImageView,actionBarThirdImageView,actionBarFourthImageView,previewPlayImageView;
	private TextView headlineTextView, descriptionTextView, hashTagsTextView, countViewTextView, countSharesTextView, impactTypeTextView, ratingTextView;
	private UploadPost postPreview;
	private DataHelper dataHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_display);
		dataHelper = new DataHelper(PreviewPostActivity.this);

		previrePostsImageView = (ImageView) findViewById(R.id.singleFeedImageView);
		headlineTextView = (TextView) findViewById(R.id.singleFeedTitleTextView);
		descriptionTextView = (TextView) findViewById(R.id.singleFeedDescriptionTextView);
		hashTagsTextView = (TextView) findViewById(R.id.singleFeedHashTagsTextView);
		countViewTextView = (TextView) findViewById(R.id.singleFeedViewsTextView);
		countSharesTextView = (TextView) findViewById(R.id.singleFeedCommentsTextView);
		impactTypeTextView = (TextView) findViewById(R.id.singleFeedImpactTextView);
		ratingTextView = (TextView) findViewById(R.id.singleFeedRatingtextView);
		actionBarFirstImageView=(ImageView)findViewById(R.id.actionBarLeftImageView);
		actionBarSecondImageView=(ImageView)findViewById(R.id.actionBarSecondImageView);
		actionBarThirdImageView=(ImageView)findViewById(R.id.actionBarThirdImageView);
		actionBarFourthImageView=(ImageView)findViewById(R.id.actionBarFourthImageView);
		previewPlayImageView=(ImageView)findViewById(R.id.singleFeedPlayImageView);
		
		actionBarFirstImageView.setVisibility(View.GONE);
		actionBarSecondImageView.setVisibility(View.GONE);
		actionBarThirdImageView.setVisibility(View.GONE);
		actionBarFourthImageView.setVisibility(View.GONE);

		if (null != DataCache.getUploadPost()) {
			postPreview = DataCache.getUploadPost();

			headlineTextView.setText(postPreview.getHeadLine());
			descriptionTextView.setText(postPreview.getDescription());
			
			String hashTags=postPreview.getHashTags();
			hashTags=hashTags.replace(' ', '#');
			
			hashTagsTextView.setText("#"+hashTags);
			
			countViewTextView.setText("0 Views");
			countSharesTextView.setText("0 Shares");
			impactTypeTextView.setText(postPreview.getImpactString()+" Impact");
			ratingTextView.setText("0.0");

			if (null != postPreview.getFiles())
				if (!postPreview.isVideo()) {
					CountDownTimer countDownTimer;
					long timerValue = postPreview.getFiles().size() * 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC;

					countDownTimer = new CountDownTimer(timerValue, 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC) {

						int counter = 0;

						@Override
						public void onTick(long millisUntilFinished) {
							previrePostsImageView.setImageURI(postPreview.getFiles().get(counter));
							counter++;
						}

						@Override
						public void onFinish() {
							counter = 0;
							this.start();
						}
					};
					countDownTimer.start();
				} else {
					previewPlayImageView.setVisibility(View.VISIBLE);
					Bitmap thumb = ThumbnailUtils.createVideoThumbnail(postPreview.getFiles().get(0).toString(), MediaStore.Images.Thumbnails.MINI_KIND);
					Matrix matrix = new Matrix();
					Bitmap bitmap = Bitmap.createBitmap(thumb, 0, 0, thumb.getWidth(), thumb.getHeight(), matrix, true);

					previrePostsImageView.setImageBitmap(bitmap);
				}
		}

	}

}
