package com.sudosaints.android.mojoscribe;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class ContactUsActivity extends Activity {
	private UIHelper uiHelper;
	private EditText name, email, number, message;
	private TextView send, mandatoryString, nameString, emailString, phoneString, messageString;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		uiHelper = new UIHelper(ContactUsActivity.this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.CONTACT_US, R.layout.contact_us);

		name = (EditText) findViewById(R.id.contactUsNameEditText);
		email = (EditText) findViewById(R.id.contactUsEmailEditText);
		number = (EditText) findViewById(R.id.contactUsPhoneEditText);
		message = (EditText) findViewById(R.id.contactUsMessageEditText);
		send = (TextView) findViewById(R.id.contactUsSendMessageTextView);
		mandatoryString = (TextView) findViewById(R.id.contactUsManditoryTextView);
		nameString = (TextView) findViewById(R.id.contactUsNameStringTextView);
		emailString = (TextView) findViewById(R.id.contactUsEmailStringTextView);
		phoneString = (TextView) findViewById(R.id.contactUsPhoneStringTextView);
		messageString = (TextView) findViewById(R.id.contactUsmessageStringTextView);

		name.setTypeface(typeface);
		email.setTypeface(typeface);
		number.setTypeface(typeface);
		message.setTypeface(typeface);
		send.setTypeface(typeface);
		mandatoryString.setTypeface(typeface);
		nameString.setTypeface(typeface);
		emailString.setTypeface(typeface);
		phoneString.setTypeface(typeface);
		messageString.setTypeface(typeface);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (name.getText().toString().length() > 0) {
					if (name.getText().toString().length() > 0) {
						if (name.getText().toString().length() > 0) {
							new DoContactUs(ContactUsActivity.this, name.getText().toString(), email.getText().toString(), number.getText().toString(), message.getText().toString()).execute();
						} else {
							uiHelper.CustomToast("Type Message");
						}
					} else {
						uiHelper.CustomToast("Type Number");
					}
				} else {
					uiHelper.CustomToast("Type Name");
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	private class DoContactUs extends AsyncTask<Void, Void, ApiResponse> {
		private Context context;
		private String name, email, phone, msg;
		private ProgressDialog progressDialog;
		private LoadingDialog loadingDialog;

		public DoContactUs(Context context, String name, String email, String phone, String msg) {
			super();
			this.context = context;
			this.name = name;
			this.email = email;
			this.phone = phone;
			this.msg = msg;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Sending data...");
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
			ApiResponse apiResponse = apiRequestHelper.doConatctUs(name, email, phone, msg);
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
				uiHelper.CustomToast("Send successful");
				startActivity(new Intent(ContactUsActivity.this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
				finish();

			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
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
