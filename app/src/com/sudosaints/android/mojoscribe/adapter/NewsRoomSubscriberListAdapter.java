package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.sudosaints.android.mojoscribe.model.NewsRoomPosts;
import com.sudosaints.android.mojoscribe.model.NewsRoomSubscription;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class NewsRoomSubscriberListAdapter extends BaseAdapter {

	private List<NewsRoomSubscription> newsRoomSubscriptions;
	private Context context;
	private Drawable defaultDrawable;
	private Typeface typeface;

	public NewsRoomSubscriberListAdapter(Context context, List<NewsRoomSubscription> newsRoomSubscriptions) {
		this.context = context;
		this.newsRoomSubscriptions = newsRoomSubscriptions;
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsRoomSubscriptions.size();
	}

	@Override
	public NewsRoomSubscription getItem(int arg0) {
		// TODO Auto-generated method stub
		return newsRoomSubscriptions.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final NewsRoomSubscription newsRoomSubscription = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.newsroom_subscribe_layout, parent, false);
		}

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, NewsRoomActivity.class);
				intent.putExtra(IntentExtras.INTENT_USER_ID, newsRoomSubscription.getId());
				context.startActivity(intent);
			}
		};
		ImageView userImage;
		TextView userName, userSubscribedNo, userRepoterLevel;

		userImage = (ImageView) convertView.findViewById(R.id.newsRoomUserImageView);
		userName = (TextView) convertView.findViewById(R.id.newsRoomUserNameTextView);
		userSubscribedNo = (TextView) convertView.findViewById(R.id.newsRoomSubscribersTextView);
		userRepoterLevel = (TextView) convertView.findViewById(R.id.newsRoomRepoterLevelTextView);

		UrlImageViewHelper.setUrlDrawable(userImage, newsRoomSubscription.getImageUrl() + "", R.drawable.profile);
		userName.setText(newsRoomSubscription.getUsername());
		userSubscribedNo.setText("Subscribers : "+newsRoomSubscription.getSubscribers());
		userRepoterLevel.setText("Repoter level : "+newsRoomSubscription.getRepoterLevel());

		userName.setTypeface(typeface);
		userSubscribedNo.setTypeface(typeface);
		userRepoterLevel.setTypeface(typeface);

		userImage.setOnClickListener(onClickListener);
		userName.setOnClickListener(onClickListener);
		userSubscribedNo.setOnClickListener(onClickListener);
		userRepoterLevel.setOnClickListener(onClickListener);
		return convertView;
	}

}
