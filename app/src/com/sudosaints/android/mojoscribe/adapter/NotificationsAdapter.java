package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.content.Context;
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
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.model.Notification;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class NotificationsAdapter extends BaseAdapter {

	private List<Notification> notifications;
	private Context context;
	private Logger logger;
	private Drawable defaultDrawable;
	private OnNotificationSelected onNotificationSelected;
	private Typeface typeface;

	public static interface OnNotificationSelected {
		public void onSelect(int notificationId, String actionType, int actionId);
	}

	public NotificationsAdapter(Context context, OnNotificationSelected onNotificationSelected, List<Notification> notifications) {
		this.context = context;
		this.notifications = notifications;
		this.onNotificationSelected = onNotificationSelected;
		this.logger = new Logger(context);
		defaultDrawable = this.context.getResources().getDrawable(R.drawable.background);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notifications.size();
	}

	@Override
	public Notification getItem(int arg0) {
		// TODO Auto-generated method stub
		return notifications.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Notification notification = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.notification_row, parent, false);
		}

		ImageView actionImageView;
		TextView actionTextView;

		actionImageView = (ImageView) convertView.findViewById(R.id.notificationRowImageView);
		actionTextView = (TextView) convertView.findViewById(R.id.notificationRowTextView);

		actionTextView.setTypeface(typeface);

		UrlImageViewHelper.setUrlDrawable(actionImageView, notification.getImageUrl(), R.drawable.mojoloading);
		actionTextView.setText(notification.getNotificationText());

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				onNotificationSelected.onSelect(notification.getId(), notification.getActionTypeString(), notification.getActionId());
				// logger.info(notification.getId()+"    "+
				// notification.getActionTypeString()+
				// "    "+notification.getActionId()+"msg from Onclick");
			}
		};
		actionTextView.setOnClickListener(clickListener);
		actionImageView.setOnClickListener(clickListener);

		return convertView;
	}

}
