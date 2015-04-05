package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.NotificationsAdapter;
import com.sudosaints.android.mojoscribe.adapter.NotificationsAdapter.OnNotificationSelected;
import com.sudosaints.android.mojoscribe.model.Notification;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.IntentExtras;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class NotificationActivity extends Activity {

	private List<Notification> notifications;
	private ListView notificationListView;
	private TextView noNotificationTextView;
	private UIHelper uiHelper;
	private NotificationsAdapter notificationsAdapter;
	private Preferences preferences;
	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.rightDrawer.toggleMenu();
		}
	};
	OnNotificationSelected onNotificationSelected = new OnNotificationSelected() {

		@Override
		public void onSelect(int notificationId, String actionType, int actionId) {
			// TODO Auto-generated method stub

			if (actionType.equalsIgnoreCase("user")) {
				Intent intent = new Intent(NotificationActivity.this, NewsRoomActivity.class);
				intent.putExtra(IntentExtras.INTENT_USER_ID, actionId);
				startActivity(intent);

			} else {
				Intent intent = new Intent(NotificationActivity.this, SingleFeedActivity.class);
				intent.putExtra(IntentExtras.INTENT_FEED_ID, actionId);
				startActivity(intent);

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_activity);
		uiHelper = new UIHelper(NotificationActivity.this);
		preferences = new Preferences(NotificationActivity.this);
		preferences.setActiveNotification(false);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.NOTIFICATIONS, R.layout.notification_activity);

		notificationListView = (ListView) findViewById(R.id.notificationlistView);
		noNotificationTextView = (TextView) findViewById(R.id.notificationNoNotificationTextView);

		new GetNotifications(NotificationActivity.this).execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	private void displayNotification() {
		notificationsAdapter = new NotificationsAdapter(NotificationActivity.this, onNotificationSelected, notifications);
		notificationListView.setAdapter(notificationsAdapter);
	}

	class GetNotifications extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetNotifications(Context context) {
			super();
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}

		@Override
		protected ApiResponse doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			ApiRequestHelper apiRequestHelper = new ApiRequestHelper(context);
			ApiResponse apiResponse = apiRequestHelper.getNotifications();
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
				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				notifications = DataMapParser.getNotifications(data);
				displayNotification();
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
				noNotificationTextView.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}
}
