package com.sudosaints.android.mojoscribe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sudosaints.android.mojoscribe.AboutUsActivity;
import com.sudosaints.android.mojoscribe.AnonymousFeedActivity;
import com.sudosaints.android.mojoscribe.BreakingActivity;
import com.sudosaints.android.mojoscribe.CategoriesListActivity;
import com.sudosaints.android.mojoscribe.ContactUsActivity;
import com.sudosaints.android.mojoscribe.DashBoardActivity;
import com.sudosaints.android.mojoscribe.DraftActivity;
import com.sudosaints.android.mojoscribe.FeedsActivity;
import com.sudosaints.android.mojoscribe.HashTagsListActivity;
import com.sudosaints.android.mojoscribe.LocationBasedFeedActivity;
import com.sudosaints.android.mojoscribe.MojoPicksActivity;
import com.sudosaints.android.mojoscribe.NewsRoomActivity;
import com.sudosaints.android.mojoscribe.NotificationActivity;
import com.sudosaints.android.mojoscribe.PollsActivity;
import com.sudosaints.android.mojoscribe.PostActivity;
import com.sudosaints.android.mojoscribe.PostsListActivity;
import com.sudosaints.android.mojoscribe.Preferences;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.RecentNewsActivity;
import com.sudosaints.android.mojoscribe.UserSettingsActivity;
import com.sudosaints.android.mojoscribe.model.DrawerOption;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;

public class DrawerOptionAdapter extends BaseAdapter {

	private List<DrawerOption> drawerOptions;
	private Activity context;
	private OnOptionSelected onOptionSelected;
	private DrawerOptionAction currentScreen;
	private Preferences preferences;
	private Typeface typeface;

	public static interface OnOptionSelected {
		public void onSelect();
	}

	public DrawerOptionAdapter(Activity context, List<DrawerOption> drawerOptions, OnOptionSelected onOptionSelected, DrawerOptionAction action) {
		this.context = context;
		this.drawerOptions = drawerOptions;
		this.onOptionSelected = onOptionSelected;
		this.currentScreen = action;
		this.typeface = TypefaceLoader.get(context, "fonts/SansRegular.ttf");
		preferences = new Preferences(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drawerOptions.size();
	}

	@Override
	public DrawerOption getItem(int position) {
		// TODO Auto-generated method stub
		return drawerOptions.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final DrawerOption drawerOption = getItem(position);
		if (view == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = li.inflate(R.layout.drawer_option_row, parent, false);
		}

		Button optionButton;
		LinearLayout optionLayout;

		optionLayout = (LinearLayout) view.findViewById(R.id.OptionLayout);
		optionButton = (Button) view.findViewById(R.id.drawerOptionButton);
		optionButton.setTypeface(typeface);
		// if (drawerOption.getLable().equalsIgnoreCase("upload")) {
		// optionLayout.setBackgroundResource(R.drawable.drawer_button_green_background);

		// optionButton.setText(drawerOption.getLable());
		// } else {

		if ((position % 2) == 1) {
			optionLayout.setBackgroundColor(Color.parseColor("#CE0000"));
			// (oddDrawable);
		} else {
			optionLayout.setBackgroundColor(Color.parseColor("#B90000"));
		}

		optionButton.setText(drawerOption.getLable());
		// }
		if (currentScreen == getItem(position).getOptionAction()) {
			optionButton.setCompoundDrawablesWithIntrinsicBounds(drawerOption.getImageId(), 0, 0, 0);
		} else {
			optionButton.setCompoundDrawablesWithIntrinsicBounds(drawerOption.getImageId(), 0, 0, 0);
		}
		optionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// onOptionSelected.onSelect();
				if (currentScreen == getItem(position).getOptionAction()) {
					onOptionSelected.onSelect();
				} else {

					DrawerOption drawerOption = (DrawerOption) getItem(position);
					switch (drawerOption.getOptionAction()) {
					case ABOUT_US:
						String url = ((Activity) context).getResources().getString(R.string.AboutUs);
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						context.startActivity(i);
						break;
					case CATEGOIES:
						context.startActivity(new Intent(context, CategoriesListActivity.class));
						((Activity) context).finish();
						break;
					case CONTACT_US:
						context.startActivity(new Intent(context, ContactUsActivity.class));
						((Activity) context).finish();
						break;
					case HOME:
						if (preferences.getUserId() > 0) {
							context.startActivity(new Intent(context, DashBoardActivity.class));
							((Activity) context).finish();
						} else {
							context.startActivity(new Intent(context, FeedsActivity.class));
							((Activity) context).finish();
						}
						break;
					case FEATURED:
						break;
					case LOGOUT:
						preferences.logOut();
						context.startActivity(new Intent(context, FeedsActivity.class));
						((Activity) context).finish();
						break;
					case POLLS:
						context.startActivity(new Intent(context, PollsActivity.class));
						((Activity) context).finish();
						break;
					case UPLOAD:
						context.startActivity(new Intent(context, PostActivity.class));
						((Activity) context).finish();
						break;
					case BREAKING:
						context.startActivity(new Intent(context, BreakingActivity.class));
						((Activity) context).finish();
						break;
					case DRAFTS:
						context.startActivity(new Intent(context, DraftActivity.class));
						break;
					case MOJO_PICKS:
						context.startActivity(new Intent(context, MojoPicksActivity.class));
						((Activity) context).finish();
						break;
					case NEWSROOM:
						context.startActivity(new Intent(context, NewsRoomActivity.class));
						((Activity) context).finish();
						break;
					case POSTS:
						context.startActivity(new Intent(context, PostsListActivity.class));
						((Activity) context).finish();
						break;
					case RECENT_NEWS:
						context.startActivity(new Intent(context, RecentNewsActivity.class));
						((Activity) context).finish();
						break;
					case SETTINGS:
						context.startActivity(new Intent(context, UserSettingsActivity.class));
						((Activity) context).finish();
						break;
					case NOTIFICATIONS:
						context.startActivity(new Intent(context, NotificationActivity.class));
						((Activity) context).finish();
						break;

					case TRENDING_HASH_TAGS:
						context.startActivity(new Intent(context, HashTagsListActivity.class));
						((Activity) context).finish();
						break;
					case ANONYMOUS:
						context.startActivity(new Intent(context, AnonymousFeedActivity.class));
						((Activity) context).finish();
						break;
					case LOCATION_BASED:
						context.startActivity(new Intent(context, LocationBasedFeedActivity.class));
						((Activity) context).finish();
						break;

					default:
						break;
					}
				}
			}
		});

		return view;

	}
}
