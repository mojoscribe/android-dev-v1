package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.TrendingHashTagsActivity;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class HashTagsOptionAdapter extends BaseAdapter {

	private List<IdValue> options;
	private Context context;
	private Logger logger;
	private Typeface typeface;

	public HashTagsOptionAdapter(Context context, List<IdValue> options) {
		this.context = context;
		this.options = options;
		logger = new Logger(context);
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return options.size();
	}

	@Override
	public IdValue getItem(int arg0) {
		// TODO Auto-generated method stub
		return options.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final IdValue option = getItem(position);
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.hashtags_row, parent, false);
		}

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent catgIntent = new Intent(context, TrendingHashTagsActivity.class);
				catgIntent.putExtra(IntentExtras.INTENT_HASHTAG_TAG, option.getName());
				((Activity) context).startActivity(catgIntent);
				((Activity) context).finish();
			}
		};
		TextView textView = (TextView) convertView.findViewById(R.id.hashtagsTextView);
		textView.setTypeface(typeface);
		ImageButton button = (ImageButton) convertView.findViewById(R.id.hashtagsImageButton);
		textView.setText(option.getName());
		textView.setOnClickListener(clickListener);
		button.setOnClickListener(clickListener);

		return convertView;
	}

}
