package com.sudosaints.android.mojoscribe;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.sudosaints.android.mojoscribe.model.User;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;
import com.sudosaints.android.mojoscribe.util.ResultStatus;
import com.sudosaints.android.mojoscribe.view.TwitterOAuthView;
import com.sudosaints.android.mojoscribe.view.TwitterOAuthView.Result;

public class TwitterOAuthActivity extends Activity implements TwitterOAuthView.Listener {
	// Replace values of the parameters below with your own.
	 private static final String CONSUMER_KEY = "aazIMYuEJFxgipZWo5Yw";
	    private static final String CONSUMER_SECRET = "GE8fqJw7NhABtJNbdF99y3Y960BdYA33wyz7fVnaY";
	    private static final String CALLBACK_URL = "https://www.sudosaints.com";
	    private static final boolean DUMMY_CALLBACK_URL = true;

	private TwitterOAuthView view;
	private boolean oauthStarted;
	Logger logger;
	Twitter twitter;
	User user;
	boolean isDestroyed = false;
	String linkString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create an instance of TwitterOAuthView.
		view = new TwitterOAuthView(this);
		logger = new Logger(this);

		 if(getIntent().hasExtra(IntentExtras.INTENT_LINK)) {
	        	linkString = (String)getIntent().getStringExtra(IntentExtras.INTENT_LINK);
	        	setContentView(view);
	        	oauthStarted = false;
	        } else {
	        	setResult(RESULT_CANCELED);
	        	finish();
	        }
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (oauthStarted) {
			return;
		}

		oauthStarted = true;

		// Start Twitter OAuth process. Its result will be notified via
		// TwitterOAuthView.Listener interface.
		view.start(CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL, DUMMY_CALLBACK_URL, this);
	}

	public void onSuccess(TwitterOAuthView view, AccessToken accessToken) {
		// The application has been authorized and an access token
		// has been obtained successfully. Save the access token
		// for later use.
		showMessage("Authorized by " + accessToken.getScreenName());

		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(CONSUMER_KEY);
		configurationBuilder.setOAuthConsumerSecret(CONSUMER_SECRET);
		configurationBuilder.setDebugEnabled(true);
		twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
		twitter.setOAuthAccessToken(accessToken);
		new ShareTweet().execute();
	}

	public void onFailure(TwitterOAuthView view, Result result) {
		// Failed to get an access token.
		showMessage("Failed due to " + result);
	}

	private void showMessage(String message) {
		// Show a popup message.
		//Toast.makeText(this,  message, Toast.LENGTH_LONG).show();
	}

	private class ShareTweet extends AsyncTask<Void, Void, ResultStatus> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ResultStatus doInBackground(Void... params) {
			ResultStatus resultStatus = null;
			try {
				// TODO: Get status data to be updated on twitter
				StatusUpdate statusUpdate = new StatusUpdate(linkString);

				twitter4j.Status status = twitter.updateStatus(statusUpdate);
				logger.debug(status.toString());
				logger.debug("StatusText: " + status.getText());
				resultStatus = new ResultStatus(true, "Success!!");
			} catch (TwitterException e) {
				e.printStackTrace();
				resultStatus = new ResultStatus(false, e.getErrorMessage());
				return resultStatus;
			}
			return resultStatus;
		}

		@Override
		protected void onPostExecute(ResultStatus result) {
			super.onPostExecute(result);
			Intent intent = new Intent();
			intent.putExtra(IntentExtras.TWITTER_RESULT_EXTRA, result.getStatusMessage());
			if (result.isSuccess()) {
				setResult(RESULT_OK, intent);
			} else {
				setResult(RESULT_CANCELED, intent);
			}
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isDestroyed = true;
	}

}