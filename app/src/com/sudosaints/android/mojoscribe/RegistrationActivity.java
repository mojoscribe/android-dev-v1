package com.sudosaints.android.mojoscribe;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.CommonUtil;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class RegistrationActivity extends Activity {

	private EditText usernameEditText, emailEditText, passwordEditText, confirnPasswordEditText;
	private TextView registerTextView, actionBarTitleEditText;
	private Preferences preferences;
	private Logger logger;
	private UIHelper uiHelper;
	private Typeface typeface;
	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.leftDrawer.toggleMenu();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		logger = new Logger(RegistrationActivity.this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		uiHelper = new UIHelper(RegistrationActivity.this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.REGISTER, R.layout.activity_signup);
		usernameEditText = (EditText) findViewById(R.id.registerUsernameEditText);
		emailEditText = (EditText) findViewById(R.id.registerEmailEditText);
		passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
		confirnPasswordEditText = (EditText) findViewById(R.id.registerConfirmPasswordEditText);
		actionBarTitleEditText = (TextView) findViewById(R.id.actionBarAppTextView);

		registerTextView = (TextView) findViewById(R.id.registerSignupTextView);
		preferences = new Preferences(this);

		usernameEditText.setTypeface(typeface);
		emailEditText.setTypeface(typeface);
		passwordEditText.setTypeface(typeface);
		confirnPasswordEditText.setTypeface(typeface);
		registerTextView.setTypeface(typeface);
		actionBarTitleEditText.setTypeface(typeface);

		registerTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doRegistrartionUser();
			}
		});

		confirnPasswordEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (EditorInfo.IME_ACTION_GO == actionId) {
					doRegistrartionUser();
				}
				return false;
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	private void doRegistrartionUser() {

		String usernameStr = "", passwordStr = "", cnfPasswordStr = "", emailStr = "";
		if (usernameEditText.getText().length() > 0) {
			usernameStr = usernameEditText.getText().toString();
			if (emailEditText.getText().length() > 0 && CommonUtil.validateEmail(emailEditText.getText().toString())) {
				emailStr = emailEditText.getText().toString();

				if (passwordEditText.getText().length() > 0) {
					passwordStr = passwordEditText.getText().toString();
					if (confirnPasswordEditText.getText().length() > 0 && confirnPasswordEditText.getText().toString().equals(passwordStr)) {
						cnfPasswordStr = confirnPasswordEditText.getText().toString();

						User user = new User(0, usernameStr, passwordStr, emailStr, null, null, null);

						new RegisterAsyncTask(RegistrationActivity.this, user).execute();

					} else
						uiHelper.CustomToast("Password and Confirm password do not match.");
				} else
					uiHelper.CustomToast("Please enter valid password.");
			} else
				uiHelper.CustomToast("Please enter valid email-id.");
		} else
			uiHelper.CustomToast("Please enter valid username.");

	}

	private class RegisterAsyncTask extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private ProgressDialog progressDialog;
		private User user;
		private LoadingDialog loadingDialog;

		public RegisterAsyncTask(Context context, User user) {
			super();
			this.context = context;
			this.user = user;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Registering...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected ApiResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doRegisterUser(user);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();

				Map<String, Object> dataValues = (Map<String, Object>) dataMap.get("data");

				preferences.setAccessToken(dataValues.get("authToken").toString());

				preferences.setId(Long.parseLong(dataValues.get("userId") + ""));
				uiHelper.CustomToast("Registred");
				startActivity(new Intent(RegistrationActivity.this, UserSettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
				finish();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}

		}
	}
}