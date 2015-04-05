package com.sudosaints.android.mojoscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.SettingsOptionAdapter;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.Constants;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataHelper;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class UserSettingsActivity extends Activity {

	private User user, userOld;
	private TabHost tabHost;
	private ImageView userPicImageView;
	private Drawable defaultDrawable;
	private File mediaFile;
	private TextView userNameTextView, userLocationTextView, profileResetSettingsTextView, profileSaveSettingsTextView, prefSaveSettingsTextView, prefResetSettingsTextView, emailIdTextView, categoriesStringTextView, notificationSettingsStringTextView, genderStringTextView;
	private EditText firstNameEditText, lastNameEditText, userAboutEditText, contactNoEditText;
	private RadioButton maleRadioButton, femaleRadioButton;
	private List<Uri> mediaUris;
	private com.sudosaints.android.mojoscribe.view.CustomListView categoriesListView;
	private SettingsOptionAdapter categoriesadapter;
	private AutoCompleteTextView userCountryTextView, userCityTextView, userPeferedPlacesTextView;
	private List<IdValue> countryList, cityList, placesList;
	private ArrayAdapter<String> countryAdapter, cityAdapter, preferancePlacesAdapter;
	private List<String> listCountry, listCity, listPlaces;
	private com.sudosaints.android.mojoscribe.view.FlowLayout flowLayout;
	private UIHelper uiHelper;
	private CheckBox mobileNotiCheckBox, emailNotiCheckBox;
	private Typeface typeface;
	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.rightDrawer.toggleMenu();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		uiHelper = new UIHelper(UserSettingsActivity.this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.SETTINGS, R.layout.settings_layout);

		defaultDrawable = getResources().getDrawable(R.drawable.background);
		mediaUris = new ArrayList<Uri>();

		tabHost = (TabHost) findViewById(R.id.settingTabHost);
		tabHost.setup();
		// map tab
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TabSpec mapTabSpec1 = tabHost.newTabSpec("MAP1");
		Button tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Profile Settings");
		tabButton.setTypeface(typeface);
		mapTabSpec1.setIndicator(tabButton);
		mapTabSpec1.setContent(R.id.settingsUserProfile);

		// Map Tab
		TabSpec mapTabSpec2 = tabHost.newTabSpec("MAP2");
		tabButton = (Button) inflater.inflate(R.layout.tab_button_layout, null);
		tabButton.setText("Preference Settings");
		tabButton.setTypeface(typeface);
		mapTabSpec2.setIndicator(tabButton);
		mapTabSpec2.setContent(R.id.settingsUserPref);

		tabHost.addTab(mapTabSpec1);
		tabHost.addTab(mapTabSpec2);

		userNameTextView = (TextView) findViewById(R.id.profileSettingsAuthorNameTextView);
		userLocationTextView = (TextView) findViewById(R.id.profileSettingsAuthorLoationTextView);

		userCountryTextView = (AutoCompleteTextView) findViewById(R.id.profileSettingscountryNameTextView);
		userCityTextView = (AutoCompleteTextView) findViewById(R.id.profileSettingsCityNameTextView);
		profileResetSettingsTextView = (TextView) findViewById(R.id.profileSettingsResetTextView);
		profileSaveSettingsTextView = (TextView) findViewById(R.id.profileSettingsSaveTextView);
		prefSaveSettingsTextView = (TextView) findViewById(R.id.prefSettingsSaveTextView);
		prefResetSettingsTextView = (TextView) findViewById(R.id.prefSettingsResetTextView);
		emailIdTextView = (TextView) findViewById(R.id.prefSettingsEmailtextview);
		firstNameEditText = (EditText) findViewById(R.id.profileSettingsFirstNameEditText);
		lastNameEditText = (EditText) findViewById(R.id.profileSettingsLastNameEditText);
		userAboutEditText = (EditText) findViewById(R.id.profileSettingsAboutEditText);
		contactNoEditText = (EditText) findViewById(R.id.profileSettingsContactEditText);
		maleRadioButton = (RadioButton) findViewById(R.id.settingsMaleradio);
		femaleRadioButton = (RadioButton) findViewById(R.id.settingsFemaleradio);
		flowLayout = (com.sudosaints.android.mojoscribe.view.FlowLayout) findViewById(R.id.prefSettingsCitysFlowLayout);
		userPeferedPlacesTextView = (AutoCompleteTextView) findViewById(R.id.prefSettingsAutoCompleteTextView);
		mobileNotiCheckBox = (CheckBox) findViewById(R.id.prefsettingsMobileNotificationCheckBox);
		emailNotiCheckBox = (CheckBox) findViewById(R.id.prefSettingsEmailNotificationCheckBox);
		categoriesStringTextView = (TextView) findViewById(R.id.categoriesStringTextView);
		notificationSettingsStringTextView = (TextView) findViewById(R.id.NotificationSetting);
		userPicImageView = (ImageView) findViewById(R.id.profileSettingsAuthorImageView);
		genderStringTextView = (TextView) findViewById(R.id.GenderTextView);
		categoriesListView = (com.sudosaints.android.mojoscribe.view.CustomListView) findViewById(R.id.prefSettingsCategoriesListView);
		userCountryTextView.setThreshold(2);
		userCityTextView.setThreshold(2);
		userPeferedPlacesTextView.setThreshold(2);

		mobileNotiCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				user.setMobileNotification(isChecked);

			}
		});
		emailNotiCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				user.setEmailNotification(isChecked);

			}
		});

		prefSaveSettingsTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new doSavePreferanceSettings(UserSettingsActivity.this, user).execute();
			}
		});

		userPeferedPlacesTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 1) {
					new GetPrefPlacesAutocomplete(UserSettingsActivity.this, s.toString()).execute();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		userCountryTextView.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (s.length() > 1) {
					new GetCountriesAutocomplete(UserSettingsActivity.this, s.toString()).execute();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		userCityTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				if (s.length() > 1) {
					new GetCityAutocomplete(UserSettingsActivity.this, s.toString(), userCountryTextView.getText().toString()).execute();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		new GetSettingsData(UserSettingsActivity.this).execute();
		prefResetSettingsTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				user = userOld.getClone();
				displayProfileSettings();
			}
		});
		profileResetSettingsTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				displayProfileSettings();
			}
		});
		profileSaveSettingsTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (firstNameEditText.getText().toString().length() > 0) {
					if (lastNameEditText.getText().toString().length() > 0) {
						user.setUsername(userNameTextView.getText().toString());
						user.setFirstName(firstNameEditText.getText().toString());
						user.setLastName(lastNameEditText.getText().toString());
						user.setCountry(userCountryTextView.getText().toString());
						user.setCity(userCityTextView.getText().toString());
						user.setContactNo(contactNoEditText.getText().toString());
						user.setAbout(userAboutEditText.getText().toString());
						user.setGender(femaleRadioButton.isChecked() ? "female" : "male");
						new doSaveProfileSettings(UserSettingsActivity.this, user, mediaUris).execute();
					} else {
						uiHelper.CustomToast("Enter last name");
					}
				} else {
					uiHelper.CustomToast("Enter first name");
				}
			}
		});
		userNameTextView.setTypeface(typeface);
		userLocationTextView.setTypeface(typeface);
		profileResetSettingsTextView.setTypeface(typeface);
		profileSaveSettingsTextView.setTypeface(typeface);
		prefSaveSettingsTextView.setTypeface(typeface);
		prefResetSettingsTextView.setTypeface(typeface);
		emailIdTextView.setTypeface(typeface);
		categoriesStringTextView.setTypeface(typeface);
		firstNameEditText.setTypeface(typeface);
		lastNameEditText.setTypeface(typeface);
		userAboutEditText.setTypeface(typeface);
		contactNoEditText.setTypeface(typeface);
		maleRadioButton.setTypeface(typeface);
		femaleRadioButton.setTypeface(typeface);
		userCountryTextView.setTypeface(typeface);
		userCityTextView.setTypeface(typeface);
		userPeferedPlacesTextView.setTypeface(typeface);
		mobileNotiCheckBox.setTypeface(typeface);
		emailNotiCheckBox.setTypeface(typeface);
		notificationSettingsStringTextView.setTypeface(typeface);
		genderStringTextView.setTypeface(typeface);
	}

	void updatePlaces() {
		listPlaces = new ArrayList<String>();
		for (IdValue val : placesList) {
			listPlaces.add(val.getName());
		}
		preferancePlacesAdapter = new ArrayAdapter<String>(UserSettingsActivity.this, R.layout.custom_text_view, listPlaces);
		userPeferedPlacesTextView.setAdapter(preferancePlacesAdapter);
		userPeferedPlacesTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				userPeferedPlacesTextView.setText("");
				IdValue place = new IdValue();
				place.setId(placesList.get(arg2).getId());
				place.setName(placesList.get(arg2).getName().replace(",", ""));
				user.getPreferanceLocation().add(placesList.get(arg2).getName().replace(",", ""));
				Drawable img = getResources().getDrawable(R.drawable.cross_black);
				img.setBounds(0, 0, 30, 30);
				flowLayout.removeAllViews();
				for (final String val : user.getPreferanceLocation()) {
					final Button button = new Button(getApplicationContext());
					LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lastTxtParams.setMargins(5, 5, 5, 5);
					button.setLayoutParams(lastTxtParams);
					button.setText(" " + val + " ");
					button.setTypeface(typeface);
					button.setCompoundDrawables(null, null, img, null);
					button.setTextColor(Color.BLACK);
					button.setBackgroundResource(R.drawable.rounded_corner_button_white_background);
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							user.getPreferanceLocation().remove(val);
							button.setVisibility(View.GONE);
						}
					});
					flowLayout.addView(button);
				}
			}
		});

	}

	void updateCountry() {
		listCountry = new ArrayList<String>();
		for (IdValue val : countryList) {
			listCountry.add(val.getName());
		}
		countryAdapter = new ArrayAdapter<String>(UserSettingsActivity.this, R.layout.custom_text_view, listCountry);
		userCountryTextView.setAdapter(countryAdapter);

	}

	void updateCity() {
		listCity = new ArrayList<String>();
		for (IdValue val : cityList) {
			listCity.add(val.getName());
		}
		cityAdapter = new ArrayAdapter<String>(UserSettingsActivity.this, R.layout.custom_text_view, listCity);
		userCityTextView.setAdapter(cityAdapter);

	}

	void displayProfileSettings() {
		if (null != user) {
			userNameTextView.setText(userOld.getUsername());
			userLocationTextView.setText(userOld.getLocation());
			firstNameEditText.setText(userOld.getFirstName());
			lastNameEditText.setText(userOld.getLastName());
			userAboutEditText.setText(userOld.getAbout());
			contactNoEditText.setText(userOld.getContactNo());
			if (userOld.getGender().equalsIgnoreCase("female")) {
				femaleRadioButton.setChecked(true);
			} else {
				maleRadioButton.setChecked(true);
			}
			userCountryTextView.setText(userOld.getCountry());
			userCityTextView.setText(userOld.getCity());
			emailNotiCheckBox.setChecked(userOld.isEmailNotification());
			mobileNotiCheckBox.setChecked(userOld.isMobileNotification());
			emailIdTextView.setText(userOld.getEmail());

			UrlImageViewHelper.setUrlDrawable(userPicImageView, userOld.getImageUrl() + "", R.drawable.profile);
			userPicImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectMedia();
				}
			});

			categoriesadapter = new SettingsOptionAdapter(UserSettingsActivity.this, user.getCategoriesOptions(), userOld.getCategoriesOptions());
			categoriesListView.setAdapter(categoriesadapter);
			Drawable img = getResources().getDrawable(R.drawable.cross_black);
			img.setBounds(0, 0, 30, 30);
			flowLayout.removeAllViews();
			for (final String val : userOld.getPreferanceLocation()) {
				final Button button = new Button(getApplicationContext());
				LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				lastTxtParams.setMargins(5, 5, 5, 5);
				button.setLayoutParams(lastTxtParams);
				button.setText(" " + val + " ");
				button.setTypeface(typeface);
				button.setCompoundDrawables(null, null, img, null);
				button.setTextColor(Color.BLACK);
				button.setBackgroundResource(R.drawable.rounded_corner_button_white_background);

				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						user.getPreferanceLocation().remove(val);
						button.setVisibility(View.GONE);
					}
				});
				flowLayout.addView(button);
			}

		}

	}

	public void selectMedia() {

		final ArrayList<String> choiceList = new ArrayList<String>();
		choiceList.add("Camera");
		choiceList.add("Gallery");

		final ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.row_media_pic_dialog, choiceList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View row = convertView;
				if (row == null) {
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.row_media_pic_dialog, null);
				}

				((TextView) row.findViewById(R.id.rowTextView)).setText(choiceList.get(position));
				if (0 == position)
					((ImageView) row.findViewById(R.id.rowImageView)).setImageResource(R.drawable.icon_camera);
				else
					((ImageView) row.findViewById(R.id.rowImageView)).setImageResource(R.drawable.icon_gallery);
				return row;
			}
		};

		AlertDialog alertDialog = new AlertDialog.Builder(this).setAdapter(listAdapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					// Start Camera
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					DataHelper dataHelper = new DataHelper(UserSettingsActivity.this);
					dataHelper.createDataDirIfNotExists();
					mediaFile = new File(dataHelper.getDataDir(), Constants.MEDIA_FILE_NAME);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
					startActivityForResult(intent, Constants.REQUEST_LAUNCH_CAMERA);
				} else if (which == 1) {
					// Choose from Gallery
					Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
					startActivityForResult(intent, Constants.REQUEST_LAUNCH_GALLERY);
				}
			}
		}).setTitle("Select Media").create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (resultCode == RESULT_OK) {
			if (Constants.REQUEST_LAUNCH_CAMERA == requestCode) {

				bitmap = getSampledBitmap();
				if (null != bitmap) {
					Matrix matrix = new Matrix();
					try {
						ExifInterface exifInterface = new ExifInterface(mediaFile.getAbsolutePath());
						int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

						switch (orientation) {
						case ExifInterface.ORIENTATION_ROTATE_90:
							matrix.postRotate(90);
							break;
						case ExifInterface.ORIENTATION_ROTATE_180:
							matrix.postRotate(180);
							break;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					try {
						bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
						FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
						bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
						fileOutputStream.flush();
						fileOutputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (Constants.REQUEST_LAUNCH_GALLERY == requestCode) {
				try {
					String filePath = CommonUtil.getPath(data.getData(), this);
					mediaFile = new File(filePath);
					bitmap = getSampledBitmap();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != bitmap)
				setImageDisplay(bitmap);
		}
	}

	private Bitmap getSampledBitmap() {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public void setImageDisplay(Bitmap bitmap) {
		userPicImageView.setImageBitmap(bitmap);

		mediaUris.add(Uri.parse(mediaFile.getAbsolutePath()));

	}

	class GetSettingsData extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetSettingsData(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getUserSettings();
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				user = DataMapParser.getSettingsData(data);
				userOld = DataMapParser.getSettingsData(data);
				displayProfileSettings();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class GetCountriesAutocomplete extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private LoadingDialog loadingDialog;
		private String q;

		public GetCountriesAutocomplete(Context context, String q) {
			super();
			this.context = context;
			this.q = q;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * progressDialog = new ProgressDialog(context);
			 * progressDialog.setMessage("Signing In...");
			 * progressDialog.setCancelable(false);
			 * progressDialog.setCanceledOnTouchOutside(false);
			 * progressDialog.show();
			 */
		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getCountriesAutocomplete(q);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			if (null != loadingDialog && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				countryList = DataMapParser.getcategories(data);

				updateCountry();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class GetPrefPlacesAutocomplete extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private LoadingDialog loadingDialog;
		private String q;

		public GetPrefPlacesAutocomplete(Context context, String q) {
			super();
			this.context = context;
			this.q = q;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * progressDialog = new ProgressDialog(context);
			 * progressDialog.setMessage("Signing In...");
			 * progressDialog.setCancelable(false);
			 * progressDialog.setCanceledOnTouchOutside(false);
			 * progressDialog.show();
			 */
		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getPrefPlacesAutocomplete(q);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			if (null != loadingDialog && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				placesList = DataMapParser.getcategories(data);
				updatePlaces();

			} else {
			//	uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class GetCityAutocomplete extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private LoadingDialog loadingDialog;
		private String q;
		private String country;

		public GetCityAutocomplete(Context context, String q, String country) {
			super();
			this.context = context;
			this.q = q;
			this.country = country;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * progressDialog = new ProgressDialog(context);
			 * progressDialog.setMessage("Signing In...");
			 * progressDialog.setCancelable(false);
			 * progressDialog.setCanceledOnTouchOutside(false);
			 * progressDialog.show();
			 */
		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getCityAutocomplete(q, country);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {

			if (null != loadingDialog && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				cityList = DataMapParser.getcategories(data);

				updateCity();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class doSaveProfileSettings extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private User user;
		private List<Uri> mediaUris;

		public doSaveProfileSettings(Context context, User user, List<Uri> mediaUris) {
			super();
			this.context = context;
			this.user = user;
			this.mediaUris = mediaUris;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Saving...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doSaveProfileSettings(user, mediaUris);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");

				uiHelper.CustomToast("Information saved");

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class doSavePreferanceSettings extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		private User user;

		public doSavePreferanceSettings(Context context, User user) {
			super();
			this.context = context;
			this.user = user;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Saving...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.setPreferanceSettings(user);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");

				uiHelper.CustomToast("Information Saved");

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResumeActivity();
	}

}
