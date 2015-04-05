package com.sudosaints.android.mojoscribe;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import com.google.android.gcm.GCMBaseIntentService;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.Logger;

public class GCMIntentService extends GCMBaseIntentService {

	private Context context;
	private String deviceId = "";
	private Preferences preferences;
	private String userId = "";
	static private String type;
	static private int typeId;

	@Override
	protected void onError(Context context, String errorId) {
		Log.d(Logger.tag, "Received error: " + errorId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(Logger.tag, "Received message. Extras: " + intent.getExtras());

		generateNotification(context, intent.getExtras());

	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.d(Logger.tag, "Device registered: regId = " + registrationId);
		// ServerUtilities.register(context, registrationId);
		this.context = context;
		this.deviceId = registrationId;
		preferences = new Preferences(context);
		this.userId = preferences.getUserId() + "";
		preferences.setGcmId(registrationId);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {

	}

	private String getEmail(Context context) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				return account.name;
			}
		}
		return null;
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, Bundle bundle) {
		String notificationMessage = "New Message received";
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		try {

			// bundle.get("action")
			Map<String, Object> map = new ObjectMapper().readValue(bundle.getString("action"), Map.class);
			type=map.get("type").toString();
			typeId=Integer.parseInt(map.get("id").toString());
			
			Log.d("bundel", bundle.toString());
			notificationMessage = bundle.getString("msg");
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Notification notification = new Notification(icon, notificationMessage, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent;
		new Preferences(context).setActiveNotification(true);
		if(type.equalsIgnoreCase("user")){
			notificationIntent = new Intent(context, NewsRoomActivity.class);
			notificationIntent.putExtra(IntentExtras.INTENT_USER_ID, typeId);
			notificationIntent.putExtra(IntentExtras.INTENT_ACTIVE_NOTIFICATION, true);
			
		}else{
			notificationIntent = new Intent(context, SingleFeedActivity.class);
			notificationIntent.putExtra(IntentExtras.INTENT_FEED_ID, typeId);
			notificationIntent.putExtra(IntentExtras.INTENT_ACTIVE_NOTIFICATION, true);
		}
		
		
		
		
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, title, notificationMessage, intent);
		
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 /* notification.flags |= Notification.FLAG_NO_CLEAR;
		 */

		notificationManager.notify((int) Calendar.getInstance().getTimeInMillis(), notification);
	}

}
