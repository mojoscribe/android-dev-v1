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
import com.sudosaints.android.mojoscribe.NewsRoomActivity;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.SingleFeedActivity;
import com.sudosaints.android.mojoscribe.model.Feed;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class PostAnonymousListAdapter extends BaseAdapter {

	private List<Feed> feeds;
	private Context context;
	private Drawable defaultDrawable;
	private Logger logger;
	public boolean hasNewPosts;
	private FetchNextPagePostsListener fetchNextPagePostsListener;
	private Typeface typeface;

	public interface FetchNextPagePostsListener {
		public void fetchNextPagePosts();
	}

	public PostAnonymousListAdapter(Context context, List<Feed> feeds, FetchNextPagePostsListener fetchNextPagePostsListener) {
		this.context = context;
		this.feeds = feeds;
		this.fetchNextPagePostsListener = fetchNextPagePostsListener;
		typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		logger = new Logger(context);
		this.hasNewPosts = true;
	}

	public void setPostsList(List<Feed> nesFeeds) {
		/* feeds.addAll(nesFeeds); */
		for (Feed feed : nesFeeds) {
			this.feeds.add(feed);
		}
		if (nesFeeds.size() < 10) {
			hasNewPosts = false;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feeds.size();
	}

	@Override
	public Feed getItem(int arg0) {
		// TODO Auto-generated method stub
		return feeds.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (hasNewPosts && position == (feeds.size() - 1)) {
			fetchNextPagePostsListener.fetchNextPagePosts();
		}

		final Feed feed = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.feed_single_anonymous_item, parent, false);

			ViewHolder viewHolder = new ViewHolder();
			convertView.setTag(viewHolder);
		}

		TextView feedRatingTextView, feedTitleTextView, feedViewsTextView, feedCommentsTextView, feedImpactTextView;
		final ImageView feedImageView, videoPreviewImageView;
		ImageView feedOwnerImageView;

		videoPreviewImageView = (ImageView) convertView.findViewById(R.id.feedVideoPreviewImageView);

		feedRatingTextView = (TextView) convertView.findViewById(R.id.feedRatingTextView);
		feedTitleTextView = (TextView) convertView.findViewById(R.id.feedTitleTextView);
		feedViewsTextView = (TextView) convertView.findViewById(R.id.feedViewsTextView);
		feedCommentsTextView = (TextView) convertView.findViewById(R.id.feedCommentsTextView);
		feedImpactTextView = (TextView) convertView.findViewById(R.id.feedImpactTextView);
		feedRatingTextView.setTypeface(typeface);
		feedTitleTextView.setTypeface(typeface);
		feedViewsTextView.setTypeface(typeface);
		feedCommentsTextView.setTypeface(typeface);
		feedImpactTextView.setTypeface(typeface);

		feedImageView = (ImageView) convertView.findViewById(R.id.feedPostImageView);

		feedImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, SingleFeedActivity.class);
				intent.putExtra(IntentExtras.INTENT_FEED_ID, feeds.get(position).getId());
				context.startActivity(intent);

			}
		});
		/*//feedOwnerImageView = (ImageView) convertView.findViewById(R.id.feedUserImageView);
		feedOwnerImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, NewsRoomActivity.class);
				intent.putExtra(IntentExtras.INTENT_USER_ID, feeds.get(position).getAuthorId());
				context.startActivity(intent);

			}
		});*/
		if (feed.isVideo()) {
			videoPreviewImageView.setVisibility(View.VISIBLE);
		} else {
			videoPreviewImageView.setVisibility(View.INVISIBLE);
		}
	//	UrlImageViewHelper.setUrlDrawable(feedOwnerImageView, feed.getOwnerImageUrl(), R.drawable.profile);
		feedRatingTextView.setText(String.valueOf(CommonUtil.getDecimalFormat(feed.getCountRating())));
		feedTitleTextView.setText(feed.getFeedHeadLine());
		feedViewsTextView.setText(feed.getCountViews() + " Views");
		feedCommentsTextView.setText(feed.getcountShares() + " Shares");
		feedImpactTextView.setText(feed.getImapctType() + " Impact");
		feedImageView.setImageResource(feed.getImage());
		//feedOwnerImageView.setImageResource(feed.getUser());

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		if (null != viewHolder.countDownTimer) {
			viewHolder.countDownTimer.cancel();
		}

		if (feed.getFiles().size() > 1) {

			long timerValue = feed.getFiles().size() * 1050 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC;
			viewHolder.countDownTimer = new CountDownTimer(timerValue, 1000 * com.sudosaints.android.mojoscribe.util.Constants.MEDIA_FILE_ROTATE_TIME_SEC) {

				int counter = 0;

				@Override
				public void onTick(long millisUntilFinished) {
					UrlImageViewHelper.setUrlDrawable(feedImageView, feed.getFiles().get(counter).getFilePath() + "", R.drawable.mojoloading);
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
			if (feed.isVideo()) {
				try {
					UrlImageViewHelper.setUrlDrawable(feedImageView, feed.getFiles().get(0).getFileThumb() + "", defaultDrawable);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {
				try {
					UrlImageViewHelper.setUrlDrawable(feedImageView, feed.getFiles().get(0).getFilePath() + "", defaultDrawable);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

		return convertView;
	}

	static class ViewHolder {
		CountDownTimer countDownTimer;
	}

}
