package com.sudosaints.android.mojoscribe;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class LoginActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

	private TextView loginRelativeLayout, registerTextView, actionBarAppNameTextView, loginViaTextView, forgetPassTextView, fbTextView, gpTextView;
	private EditText usernameEditText, passwordEditText;
	private Preferences preferences;
	private UIHelper uiHelper;
	private DrawerOptionAction drawerOptionAction;
	private boolean isFbLogin, isTwitterLogin, isGPlusLogin;
	private RelativeLayout loginViaFb, loginGplus;
	private Logger logger;
	private String fbUserId;
	private GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked;
	private static final int RC_SIGN_IN = 0;
	private User user;
	private Typeface typeface;
	private boolean mIntentInProgress;

	private ConnectionResult mConnectionResult;
	private UiLifecycleHelper fbUiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.leftDrawer.toggleMenu();
		}
	};

	private void resolveSignInErrors() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(LoginActivity.this, RC_SIGN_IN);

			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		logger = new Logger(LoginActivity.this);
		uiHelper = new UIHelper(this);
		fbUiHelper = new UiLifecycleHelper(this, callback);
		fbUiHelper.onCreate(savedInstanceState);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");

		user = new User();
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

		uiHelper.generateDrawers(onOptionSelected, drawerOptionAction.LOGIN, R.layout.activity_login);

		preferences = new Preferences(this);

		registerTextView = (TextView) findViewById(R.id.loginRegisterTextView);
		loginRelativeLayout = (TextView) findViewById(R.id.passwordTextView);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		loginViaFb = (RelativeLayout) findViewById(R.id.loginViaFacebookRelativeLayout);
		loginGplus = (RelativeLayout) findViewById(R.id.loginGPlusLoginRelativeLayout6);
		actionBarAppNameTextView = (TextView) findViewById(R.id.actionBarAppTextView);
		loginViaTextView = (TextView) findViewById(R.id.loginLoginViaTextView);
		forgetPassTextView = (TextView) findViewById(R.id.loginForgotPassTextView);
		fbTextView = (TextView) findViewById(R.id.loginFbEditText);
		gpTextView = (TextView) findViewById(R.id.loginGpTextView);

		forgetPassTextView.setTypeface(typeface);
		registerTextView.setTypeface(typeface);
		loginRelativeLayout.setTypeface(typeface);
		usernameEditText.setTypeface(typeface);
		passwordEditText.setTypeface(typeface);
		actionBarAppNameTextView.setTypeface(typeface);
		loginViaTextView.setTypeface(typeface);
		fbTextView.setTypeface(typeface);
		gpTextView.setTypeface(typeface);

		loginGplus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				logger.debug("Gp doning");
				if (!mGoogleApiClient.isConnecting()) {
					mSignInClicked = true;
					resolveSignInErrors();
					logger.debug("Gp done");
					loginGplus.setOnClickListener(null);
				}
			}

		});

		loginViaFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFbLogin = true;
				startFbLogin(Arrays.asList("public_profile"));

				loginViaFb.setOnClickListener(null);

			}
		});

		loginRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLoginUser();

			}

		});

		passwordEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_GO) {
					doLoginUser();
				}
				return false;
			}
		});
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
		registerTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
				startActivity(i);
				finish();

			}
		});

	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void doLoginUser() {
		String userName = "", passWord = "";

		if (usernameEditText.getText().length() > 0) {
			userName = usernameEditText.getText().toString();
			if (passwordEditText.getText().length() > 0) {
				passWord = passwordEditText.getText().toString();

				new SignInAsyncTask(LoginActivity.this, userName, passWord).execute();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
		fbUiHelper.onResume();
	}

	protected void startFbLogin(List<String> permissions) {
		Session session = Session.getActiveSession();
		if (null == session) {
			session = new Session(this);
			Session.setActiveSession(session);
		}
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(permissions));
		} else {
			Session.openActiveSession(this, true, callback);
		}
	}

	protected void doFbLogout() {
		Session session = Session.getActiveSession();
		if (null != session) {
			session.closeAndClearTokenInformation();
			session = null;
			Session.setActiveSession(session);
		}
	}

	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		if (isFbLogin) {
			if (null != session && session.isOpened()) {
				logger.debug("Session opened");
				Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser me, Response response) {
						if (null == response.getError()) {
							if (response.getRequest().getSession() == session) {
								fbUserId = me.getId();
								logger.info(fbUserId + " = fb id");

								// fbAccessToken =
								// session.getAccessToken();
								// fbUsername = me.getUsername();

								if (me.asMap().containsKey("email")) {

									user.setId(Long.parseLong(me.getId()));
									user.setEmail(me.asMap().get("email").toString());
									user.setFirstName(me.getFirstName());
									user.setLastName(me.getLastName());
									user.setGender(me.asMap().get("gender").toString());

									new FBSignInAsyncTask(LoginActivity.this, user).execute();

								} else {
									doFbLogout();
									startFbLogin(Arrays.asList("email"));
								}
							}
						} else {
							logger.debug(response.getError().getErrorMessage());
						}
					}
				});
				request.executeAsync();
			} else {
				logger.debug("Session not opened");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		fbUiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		fbUiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		fbUiHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		fbUiHelper.onActivityResult(requestCode, resultCode, data);

	}

	private class SignInAsyncTask extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private String userName;
		private String password;
		private ProgressDialog progressDialog;
		private LoadingDialog loadingDialog;

		public SignInAsyncTask(Context context, String email, String password) {
			super();
			this.context = context;
			this.userName = email;
			this.password = password;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Signing In...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

			/*
			 * loadingDialog = new LoadingDialog(context); loadingDialog.show();
			 */

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doLogin(userName, password);
			return apiResponse;
		}

		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> dataValues = (Map<String, Object>) dataMap.get("data");

				preferences.setAccessToken(dataValues.get("authToken").toString());
				preferences.setId(Long.parseLong(dataValues.get("userId") + ""));
				preferences.setUserName(dataValues.get("userName") + "");
				if (null != dataValues.get("firstName")) {
					preferences.setFirstname(dataValues.get("firstName") + "");
				}
				if (null != dataValues.get("lastName")) {
					preferences.setLastname(dataValues.get("lastName") + "");
				}
				if (null != dataValues.get("profilePicture")) {
					preferences.setUrl(dataValues.get("profilePicture") + "");
				}
				uiHelper.CustomToast("Log in successful");
				finish();
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}
	}

	private class FBSignInAsyncTask extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private ProgressDialog progressDialog;
		private LoadingDialog loadingDialog;
		private User user;

		public FBSignInAsyncTask(Context context, User user) {
			super();
			this.context = context;
			this.user = user;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Signing In...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doFBLogin(user);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {

				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> dataValues = (Map<String, Object>) dataMap.get("data");

				preferences.setAccessToken(dataValues.get("authToken").toString());
				preferences.setId(Long.parseLong(dataValues.get("userId") + ""));
				preferences.setUserName(dataValues.get("userName") + "");
				if (null != dataValues.get("firstName")) {
					preferences.setFirstname(dataValues.get("firstName") + "");
				}
				if (null != dataValues.get("lastName")) {
					preferences.setLastname(dataValues.get("lastName") + "");
				}
				if (null != dataValues.get("profilePicture")) {
					preferences.setUrl(dataValues.get("profilePicture") + "");
				}

				if (Boolean.parseBoolean(dataValues.get("firstTime").toString() + "")) {
					startActivity(new Intent(context, UserSettingsActivity.class));
					uiHelper.CustomToast("Registered successful");
					finish();
				}
				finish();
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}
	}

	private class GPlusSignInAsyncTask extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private ProgressDialog progressDialog;
		private LoadingDialog loadingDialog;
		private User user;

		public GPlusSignInAsyncTask(Context context, User user) {
			super();
			this.context = context;
			this.user = user;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			  progressDialog = new ProgressDialog(context);
			  progressDialog.setMessage("Signing In...");
			  progressDialog.setCancelable(false);
			  progressDialog.setCanceledOnTouchOutside(false);
			  progressDialog.show();
			 

			/*loadingDialog = new LoadingDialog(context);
			loadingDialog.show();
*/
		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.doGPlusLogin(user);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub

			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {

				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> dataValues = (Map<String, Object>) dataMap.get("data");

				preferences.setAccessToken(dataValues.get("authToken").toString());
				preferences.setId(Long.parseLong(dataValues.get("userId") + ""));
				preferences.setUserName(dataValues.get("userName") + "");
				if (null != dataValues.get("firstName")) {
					preferences.setFirstname(dataValues.get("firstName") + "");
				}
				if (null != dataValues.get("lastName")) {
					preferences.setLastname(dataValues.get("lastName") + "");
				}
				if (null != dataValues.get("profilePicture")) {
					preferences.setUrl(dataValues.get("profilePicture") + "");
				}
				if (Boolean.parseBoolean(dataValues.get("firstTime").toString() + "")) {
					startActivity(new Intent(context, UserSettingsActivity.class));
					uiHelper.CustomToast("Registered successful");
					finish();
				}
				uiHelper.CustomToast("Log in successful");
				finish();
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the
			// user clicks
			// 'sign-in'.
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInErrors();
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mSignInClicked = false;

		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String nickname = currentPerson.getName().toString();

				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				user.setEmail(email);
				user.setUsername(personName);
				user.setImageUrl(personPhotoUrl);

				new GPlusSignInAsyncTask(LoginActivity.this, user).execute();

			} else {
				uiHelper.CustomToast("Person information is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class));
		finish();
	}
	/*
	 * @Override public void onResult(LoadPeopleResult peopleData) { if
	 * (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
	 * PersonBuffer personBuffer = peopleData.getPersonBuffer(); try { int count
	 * = personBuffer.getCount(); for (int i = 0; i < count; i++) {
	 * logger.info("Display name: " + personBuffer.get(i).getDisplayName()); } }
	 * finally { personBuffer.close(); } } else {
	 * logger.info("Error requesting visible circles: " +
	 * peopleData.getStatus()); } }
	 */

}
