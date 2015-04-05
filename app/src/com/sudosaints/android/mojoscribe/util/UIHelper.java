package com.sudosaints.android.mojoscribe.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simonvt.menudrawer.LeftDrawer;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import net.simonvt.menudrawer.Position;
import net.simonvt.menudrawer.RightDrawer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.makeramen.RoundedImageView;
import com.sudosaints.android.mojoscribe.DataCache;
import com.sudosaints.android.mojoscribe.FeedsActivity;
import com.sudosaints.android.mojoscribe.HashTagsListActivity;
import com.sudosaints.android.mojoscribe.LoginActivity;
import com.sudosaints.android.mojoscribe.MojoPicksActivity;
import com.sudosaints.android.mojoscribe.NewsRoomActivity;
import com.sudosaints.android.mojoscribe.NotificationActivity;
import com.sudosaints.android.mojoscribe.PostActivity;
import com.sudosaints.android.mojoscribe.Preferences;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.model.DrawerOption;
import com.sudosaints.android.mojoscribe.util.CommonTaskExecutor.OnPostExecute;
import com.sudosaints.android.mojoscribe.util.CommonTaskExecutor.TaskType;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;

public class UIHelper {
	private Activity activity;
	private Preferences preferences;
	public LeftDrawer leftDrawer = null;
	public RightDrawer rightDrawer = null;
	private ImageView leftDrawerImageView, actionBarLoginImageView, rightDrawerImageView, actionBarSearchImageView;
	private ImageView actionSearchBarBackImageView, actionSearchBarSearchImageView;
	private EditText actionSearchBarSearchQueryEditText;
	private View loginProfileLayout;
	private com.makeramen.RoundedImageView roundedImageView;
	private TextView userNameTextView, currentDateTextView, actionBarAppNameTextView, goToTextView, menuTextView;
	private Drawable defaultDrawable;
	private DrawerOptionAdapter leftDrawerOptionAdapter, rightDrawerOptionsAdapter;
	private ListView leftDrawerOptions, rightDrawerOptions;
	private OnOptionSelected onOptionSelected;
	private DrawerOptionAction drawerOptionAction;
	private RelativeLayout actionBar, actionSearchBar;
	private ArcMenu arcMenu;
	private Typeface typeface;

	public static interface Command {
		public void execute();
	}

	public UIHelper(Activity activity) {
		super();
		this.activity = activity;
		this.preferences = new Preferences(activity);
		this.typeface = TypefaceLoader.get(activity, "fonts/SansRegular.ttf");
	}

	private void backPress() {
		if (this.drawerOptionAction == drawerOptionAction.HOME) {
			FeedsActivity.backPress = false;
		}
	}

	public void generateDrawers(OnOptionSelected onOptionSelected, final DrawerOptionAction drawerOptionAction, int layoutId) {
		this.onOptionSelected = onOptionSelected;
		this.drawerOptionAction = drawerOptionAction;

		if (this.drawerOptionAction == DrawerOptionAction.LOGIN || this.drawerOptionAction == DrawerOptionAction.REGISTER) {

		} else {
			rightDrawer = new RightDrawer(activity);

			rightDrawer = (RightDrawer) MenuDrawer.attach(activity, MenuDrawer.MENU_DRAG_WINDOW, Position.RIGHT);
			rightDrawer.setContentView(layoutId);
			rightDrawer.setMenuView(R.layout.drawer_right);
			rightDrawer.setDropShadowColor(Color.parseColor("#20000000"));

			leftDrawer = new LeftDrawer(activity);
			leftDrawer = (LeftDrawer) MenuDrawer.attach(activity, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT);
			leftDrawer.setContentView(layoutId);
			leftDrawer.setMenuView(R.layout.drawer_left);
			leftDrawer.setDropShadowColor(Color.parseColor("#20000000"));
			actionBarLoginImageView = (ImageView) activity.findViewById(R.id.actionBarSecondImageView);
			actionBarSearchImageView = (ImageView) activity.findViewById(R.id.actionBarThirdImageView);
			leftDrawerImageView = (ImageView) activity.findViewById(R.id.actionBarLeftImageView);
			rightDrawerImageView = (ImageView) activity.findViewById(R.id.actionBarFourthImageView);
			loginProfileLayout = (View) activity.findViewById(R.id.profileIncluded);
			roundedImageView = (RoundedImageView) activity.findViewById(R.id.leftDrawerProfileImageRoundedImage);
			userNameTextView = (TextView) activity.findViewById(R.id.leftDrawerUserNameTextView);
			currentDateTextView = (TextView) activity.findViewById(R.id.leftDrawerDateTextView);
			defaultDrawable = activity.getResources().getDrawable(R.drawable.background);
			leftDrawerOptions = (ListView) activity.findViewById(R.id.leftDrawerListView);
			rightDrawerOptions = (ListView) activity.findViewById(R.id.rightDrawerListView);
			actionBar = (RelativeLayout) activity.findViewById(R.id.actionBarLayout);
			actionSearchBar = (RelativeLayout) activity.findViewById(R.id.actionSearchBarLayout);
			actionSearchBarBackImageView = (ImageView) activity.findViewById(R.id.actionSearchBarLeftImageView);
			actionSearchBarSearchImageView = (ImageView) activity.findViewById(R.id.actionSearchBarSearchImageView);
			actionSearchBarSearchQueryEditText = (EditText) activity.findViewById(R.id.actionSearchBarsearchQueryEditText);
			actionBarAppNameTextView = (TextView) activity.findViewById(R.id.actionBarAppTextView);
			goToTextView = (TextView) activity.findViewById(R.id.goToTextView);
			menuTextView = (TextView) activity.findViewById(R.id.menuTextView);

			actionBarAppNameTextView.setTypeface(typeface);
			goToTextView.setTypeface(typeface);
			userNameTextView.setTypeface(typeface);
			currentDateTextView.setTypeface(typeface);
			actionSearchBarSearchQueryEditText.setTypeface(typeface);
			menuTextView.setTypeface(typeface);

			if (this.drawerOptionAction != DrawerOptionAction.UPLOAD && this.drawerOptionAction != DrawerOptionAction.CONTACT_US && this.drawerOptionAction != DrawerOptionAction.SETTINGS && this.drawerOptionAction != DrawerOptionAction.NOTIFICATIONS && this.drawerOptionAction != DrawerOptionAction.NEWSROOM) {
				arcMenu = (ArcMenu) activity.findViewById(R.id.arc_menu);
				initArcMenu();
			}
			rightDrawerImageView.setImageResource(R.drawable.plus_icon);
			leftDrawer.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {
				@Override
				public void onDrawerStateChange(int oldState, int newState) {
					// TODO Auto-generated method stub
					backPress();
					if (newState == 8) {
						leftDrawerImageView.setImageResource(R.drawable.left_drawer_verticle);

					} else if (newState == 0) {
						leftDrawerImageView.setImageResource(R.drawable.left_drawers);
					}
				}
			});
			rightDrawer.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {
				@Override
				public void onDrawerStateChange(int oldState, int newState) {
					// TODO Auto-generated method stub
					backPress();
					if (newState == 8) {
						rightDrawerImageView.setImageResource(R.drawable.plus_active);
					} else if (newState == 0) {
						rightDrawerImageView.setImageResource(R.drawable.plus_icon);
					}
				}
			});
			leftDrawerImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (leftDrawer.getDrawerState() == leftDrawer.STATE_OPEN) {
						leftDrawer.closeMenu(true);
					} else {
						leftDrawer.openMenu(true);
					}
				}
			});
			rightDrawerImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (rightDrawer.getDrawerState() == rightDrawer.STATE_OPEN) {
						rightDrawer.closeMenu(true);

					} else {
						rightDrawer.openMenu(true);
					}
				}
			});
			actionBarSearchImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					backPress();
					actionBar.setVisibility(View.GONE);
					actionSearchBar.setVisibility(View.VISIBLE);
					rightDrawer.closeMenu(false);
					actionSearchBarSearchQueryEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

					try {
						InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.showSoftInput(null, InputMethodManager.SHOW_FORCED);

					} catch (Exception e) {
						// TODO: handle exception
					}
					actionSearchBarSearchQueryEditText.requestFocus();
				}
			});
			actionSearchBarBackImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					backPress();
					actionBar.setVisibility(View.VISIBLE);
					actionSearchBar.setVisibility(View.GONE);
					leftDrawer.closeMenu(true);
					actionBar.requestFocus();

				}
			});

			actionSearchBarSearchQueryEditText.setOnEditorActionListener(new OnEditorActionListener() {

				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					if (EditorInfo.IME_ACTION_SEARCH == actionId) {
						search();
					}
					return false;
				}
			});
			actionSearchBarSearchImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					

					
					search();
				}
			});
		}
	}

	void search() {
		// TODO Auto-generated method stub
		if (actionSearchBarSearchQueryEditText.getText().length() > 0) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(actionSearchBarSearchQueryEditText.getWindowToken(), 0);
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("query", actionSearchBarSearchQueryEditText.getText().toString());
			new CommonTaskExecutor(TaskType.SEARCH, activity, new OnPostExecute() {
				@Override
				public void onPostExecute(ApiResponse result) {
					// TODO Auto-generated method stub
					if (result.isSuccess()) {
						DataCache.setDataMap((Map<String, Object>) result.getData());
						Intent i = new Intent(activity, FeedsActivity.class);
						i.putExtra(IntentExtras.INTENT_SEARCH, IntentExtras.INTENT_SEARCH);
						i.putExtra(IntentExtras.INTENT_SEARCH_QUERY, actionSearchBarSearchQueryEditText.getText().toString());
						activity.startActivity(i);
						activity.finish();
					} else {
						CustomToast(result.getError().getMessage());
					}
				}
			}, mapData).execute();
		} else {
			CustomToast("Type a Query");
		}

	}

	private void initArcMenu() {
		// Initializes arc menu

		List<Integer> drawableIds = new ArrayList<Integer>();
		drawableIds.add(R.drawable.sat_featured_icon);
		drawableIds.add(R.drawable.sat_newsroom_icon);
		drawableIds.add(R.drawable.sat_upload_icon);
		drawableIds.add(R.drawable.sat_hash_icon);

		for (final Integer drawableId : drawableIds) {
			ImageView imageView = new ImageView(activity);
			imageView.setImageDrawable(activity.getResources().getDrawable(drawableId));

			arcMenu.addItem(imageView, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onArcChildClick(drawableId);
				}
			});
		}
	}

	private void onArcChildClick(int drawableIds) {
		// @Indraneel - TODO Handle Clicks here

		switch (drawableIds) {
		case R.drawable.sat_featured_icon:
			activity.startActivity(new Intent(activity, MojoPicksActivity.class));
			((Activity) activity).finish();
			break;

		case R.drawable.sat_newsroom_icon:
			if (preferences.getUserId() > 0) {
				activity.startActivity(new Intent(activity, NewsRoomActivity.class));
				((Activity) activity).finish();
			} else {
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				CustomToast("Login to use this feature");
			}
			break;

		case R.drawable.sat_upload_icon:
			if (preferences.getUserId() > 0) {
				activity.startActivity(new Intent(activity, PostActivity.class));
				((Activity) activity).finish();
			} else {
				Intent intent = new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				CustomToast("Login to use this feature");
			}
			break;

		case R.drawable.sat_hash_icon:
			activity.startActivity(new Intent(activity, HashTagsListActivity.class));
			((Activity) activity).finish();
			break;
		default:
			break;
		}
	}

	public void onResumeActivity() {
		if (this.drawerOptionAction == DrawerOptionAction.LOGIN || this.drawerOptionAction == DrawerOptionAction.REGISTER) {

		} else {

			if (preferences.getUserId() > 0) {
				// actionBarLoginImageView.setVisibility(View.INVISIBLE);
				loginProfileLayout.setVisibility(View.VISIBLE);
				loginProfileLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (UIHelper.this.drawerOptionAction != DrawerOptionAction.NEWSROOM) {
							Intent intent = new Intent(activity, NewsRoomActivity.class);
							activity.startActivity(intent);
						} else {
							leftDrawer.toggleMenu(true);
						}
					}
				});
				if (preferences.getActiveNotification()) {
					actionBarLoginImageView.setImageResource(R.drawable.notif_active_icon);
				} else {
					actionBarLoginImageView.setImageResource(R.drawable.notif_icon);
				}
				UrlImageViewHelper.setUrlDrawable(roundedImageView, preferences.getUrl() + "", R.drawable.profile);
				String userName, fName, lName;
				if (preferences.getFirstname().length() > 0) {
					fName = preferences.getFirstname();
					if (preferences.getLastname().length() > 0) {
						lName = preferences.getLastname();
						userNameTextView.setText(fName + " " + lName);
					} else {
						userNameTextView.setText(fName);
					}
				} else {
					userNameTextView.setText(preferences.getUserName() + "");
				}
				currentDateTextView.setText(DateHelper.getCurrentDate());
				actionBarLoginImageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (UIHelper.this.drawerOptionAction != DrawerOptionAction.NOTIFICATIONS) {
							Intent i = new Intent(activity, NotificationActivity.class);
							activity.startActivity(i);
							activity.finish();
						}
					}
				});
			} else {
				actionBarLoginImageView.setVisibility(View.VISIBLE);
				/*
				 * if (this.drawerOptionAction == DrawerOptionAction.LOGIN ||
				 * this.drawerOptionAction == DrawerOptionAction.REGISTER) {
				 * actionBarLoginImageView
				 * .setImageResource(R.drawable.user_icon_active); }
				 */
				loginProfileLayout.setVisibility(View.GONE);
				actionBarLoginImageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(activity, LoginActivity.class);
						activity.startActivity(i);
					}
				});
			}
			leftDrawerOptionAdapter = new DrawerOptionAdapter(activity, this.prepareLeftDrawerOptionLayout(), onOptionSelected, drawerOptionAction);
			leftDrawerOptions.setAdapter(leftDrawerOptionAdapter);
			rightDrawerOptionsAdapter = new DrawerOptionAdapter(activity, this.prepareRightDrawerOptionLayout(), onOptionSelected, drawerOptionAction);
			rightDrawerOptions.setAdapter(rightDrawerOptionsAdapter);
		}
	}

	public List<DrawerOption> prepareLeftDrawerOptionLayout() {
		List<DrawerOption> DrawerOptionsList = new ArrayList<DrawerOption>();
		if (preferences.getUserId() > 0) {
			DrawerOption DrawerOption1 = new DrawerOption("Upload", R.drawable.upload_icon, DrawerOptionAction.UPLOAD);
			DrawerOptionsList.add(DrawerOption1);
		}
		DrawerOption DrawerOption2 = new DrawerOption("Home", R.drawable.home_icon, DrawerOptionAction.HOME);
		DrawerOptionsList.add(DrawerOption2);
		DrawerOption DrawerOption5 = new DrawerOption("Categories", R.drawable.anchor_icon, DrawerOptionAction.CATEGOIES);
		DrawerOptionsList.add(DrawerOption5);
		DrawerOption DrawerOption6 = new DrawerOption("Polls", R.drawable.polls_icon, DrawerOptionAction.POLLS);
		DrawerOptionsList.add(DrawerOption6);
		DrawerOption DrawerOption7 = new DrawerOption("About Us", R.drawable.about_icon, DrawerOptionAction.ABOUT_US);
		DrawerOptionsList.add(DrawerOption7);
		DrawerOption DrawerOption8 = new DrawerOption("Contact Us", R.drawable.chat_icon, DrawerOptionAction.CONTACT_US);
		DrawerOptionsList.add(DrawerOption8);
		if (preferences.getUserId() > 0) {
			DrawerOption DrawerOption9 = new DrawerOption("Logout", R.drawable.logout_icon, DrawerOptionAction.LOGOUT);
			DrawerOptionsList.add(DrawerOption9);
		}
		return DrawerOptionsList;
	}

	public List<DrawerOption> prepareRightDrawerOptionLayout() {
		List<DrawerOption> DrawerOptionsList = new ArrayList<DrawerOption>();
		if (preferences.getUserId() > 0) {
			DrawerOption DrawerOption1 = new DrawerOption("Recent News", R.drawable.recent_news_icon, DrawerOptionAction.RECENT_NEWS);
			DrawerOptionsList.add(DrawerOption1);
			DrawerOption DrawerOption2 = new DrawerOption("Breaking", R.drawable.breaking_icon, DrawerOptionAction.BREAKING);
			DrawerOptionsList.add(DrawerOption2);
			DrawerOption DrawerOption3 = new DrawerOption("MojoPicks", R.drawable.goggle_icon, DrawerOptionAction.MOJO_PICKS);
			DrawerOptionsList.add(DrawerOption3);

			DrawerOption DrawerOption41 = new DrawerOption("Anonymous", R.drawable.anonymous_icon, DrawerOptionAction.ANONYMOUS);
			DrawerOptionsList.add(DrawerOption41);
			DrawerOption DrawerOption4 = new DrawerOption("Trending Hashtags", R.drawable.hash_icon, DrawerOptionAction.TRENDING_HASH_TAGS);
			DrawerOptionsList.add(DrawerOption4);
			DrawerOption DrawerOption42 = new DrawerOption("News from your Area", R.drawable.location_marker, DrawerOptionAction.LOCATION_BASED);
			DrawerOptionsList.add(DrawerOption42);
			

			DrawerOption DrawerOption5 = new DrawerOption("Newsroom", R.drawable.newsroom_user_icon, DrawerOptionAction.NEWSROOM);
			DrawerOptionsList.add(DrawerOption5);
			DrawerOption DrawerOption6 = new DrawerOption("Posts", R.drawable.posts_icon, DrawerOptionAction.POSTS);
			DrawerOptionsList.add(DrawerOption6);
			DrawerOption DrawerOption7 = new DrawerOption("Drafts", R.drawable.drafts_pencil_icon, DrawerOptionAction.DRAFTS);
			DrawerOptionsList.add(DrawerOption7);
			DrawerOption DrawerOption8 = new DrawerOption("Settings", R.drawable.settings_icon, DrawerOptionAction.SETTINGS);
			DrawerOptionsList.add(DrawerOption8);
			DrawerOption DrawerOption9 = new DrawerOption("Notification", R.drawable.notif_icon_drawer, DrawerOptionAction.NOTIFICATIONS);
			DrawerOptionsList.add(DrawerOption9);
		} else {

			DrawerOption DrawerOption1 = new DrawerOption("Recent News", R.drawable.recent_news_icon, DrawerOptionAction.RECENT_NEWS);
			DrawerOptionsList.add(DrawerOption1);
			DrawerOption DrawerOption2 = new DrawerOption("Breaking", R.drawable.breaking_icon, DrawerOptionAction.BREAKING);
			DrawerOptionsList.add(DrawerOption2);
			DrawerOption DrawerOption3 = new DrawerOption("MojoPicks", R.drawable.goggle_icon, DrawerOptionAction.MOJO_PICKS);
			DrawerOptionsList.add(DrawerOption3);
			DrawerOption DrawerOption41 = new DrawerOption("Anonymous", R.drawable.anonymous_icon, DrawerOptionAction.ANONYMOUS);
			DrawerOptionsList.add(DrawerOption41);
			DrawerOption DrawerOption4 = new DrawerOption("Trending Hashtags", R.drawable.hash_icon, DrawerOptionAction.TRENDING_HASH_TAGS);
			DrawerOptionsList.add(DrawerOption4);

		}
		return DrawerOptionsList;

	}

	public void CustomToast(String stringVal) {
		SpannableString spannableString = new SpannableString(stringVal);

		spannableString.setSpan(new RelativeSizeSpan(1.3f), 0, stringVal.length(), 0);
		spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, stringVal.length(), 0);
		Toast.makeText(activity, spannableString, Toast.LENGTH_LONG).show();
	}

}
