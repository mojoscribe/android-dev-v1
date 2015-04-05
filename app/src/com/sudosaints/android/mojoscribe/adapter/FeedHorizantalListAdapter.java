package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.SingleFeedActivity;
import com.sudosaints.android.mojoscribe.model.DashboardFeed;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class FeedHorizantalListAdapter extends BaseAdapter {

	private List<DashboardFeed> feeds;
	private Context context;
	private Drawable defaultDrawable;
	private Typeface typeface;

	public FeedHorizantalListAdapter(Context context, List<DashboardFeed> feeds) {
		this.context = context;
		this.feeds = feeds;
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feeds.size();
	}

	@Override
	public DashboardFeed getItem(int arg0) {
		// TODO Auto-generated method stub
		return feeds.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DashboardFeed feed = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.dashboard_feed_row, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			convertView.setTag(viewHolder);
		}

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SingleFeedActivity.class);
				intent.putExtra(IntentExtras.INTENT_FEED_ID, feed.getId());
				context.startActivity(intent);
			}
		};

		TextView feedElementTextView = (TextView) convertView.findViewById(R.id.dashboardFeedRowTitleTextView);
		feedElementTextView.setTypeface(typeface);
		final ImageView feedElementImageView = (ImageView) convertView.findViewById(R.id.dashboardFeedRowImageView);

		feedElementTextView.setText(feed.getHeadline());
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		if (null != viewHolder.countDownTimer) {
			viewHolder.countDownTimer.cancel();
		}
		if (feed.getFiles().size() > 1) {
			long timerValue = feed.getFiles().size() * 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC;
			viewHolder.countDownTimer = new CountDownTimer(timerValue, 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC) {
				int counter = 0;

				@Override
				public void onTick(long millisUntilFinished) {
					UrlImageViewHelper.setUrlDrawable(feedElementImageView, feed.getFiles().get(counter).getName() + "", R.drawable.mojoloading);
					counter++;
				}

				@Override
				public void onFinish() {
					counter = 0;
					this.start();
				}
			};
			viewHolder.countDownTimer.start();
		} else {
			UrlImageViewHelper.setUrlDrawable(feedElementImageView, feed.getFiles().get(0).getName(), R.drawable.mojoloading);
		}
		return convertView;
	}

	static class ViewHolder {
		CountDownTimer countDownTimer;
	}

}
