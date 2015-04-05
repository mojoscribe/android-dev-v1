package com.sudosaints.android.mojoscribe.util;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sudosaints.android.mojoscribe.Preferences;

public class CommonTaskExecutor extends AsyncTask<Void, Void, ApiResponse> {
	public static interface OnPostExecute {
		public void onPostExecute(ApiResponse apiResponse);
	}

	public static enum TaskType {
		SEARCH
	}

	private TaskType taskType;
	private Activity activity;
	private Preferences preferences;
	private Logger logger;
	private ProgressDialog progressDialog;
	private OnPostExecute onPostExecute;
	private Map<String, Object> extraData;
	private boolean showLoader = false;

	public CommonTaskExecutor(TaskType taskType, Activity activity, OnPostExecute onPostExecute, boolean showLoader) {
		super();
		this.taskType = taskType;
		this.activity = activity;
		preferences = new Preferences(this.activity);
		logger = new Logger(this.activity);
		this.onPostExecute = onPostExecute;
		this.showLoader = showLoader;
	}

	public CommonTaskExecutor(TaskType taskType, Activity activity, OnPostExecute onPostExecute, Map<String, Object> extraData) {
		super();
		this.taskType = taskType;
		this.activity = activity;
		this.onPostExecute = onPostExecute;
		this.extraData = extraData;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (showLoader) {
			progressDialog = new ProgressDialog(activity);
			switch (taskType) {
			case SEARCH:
				progressDialog.setMessage("Searching");
				break;

			default:
				break;
			}
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
	}

	@Override
	protected ApiResponse doInBackground(Void... params) {
		ApiResponse apiResponse = new ApiResponse();
		ApiRequestHelper apiRequestHelper = new ApiRequestHelper(activity);
		apiResponse.setSuccess(true);
		try {
			switch (taskType) {

			case SEARCH:
				 apiResponse = apiRequestHelper.doSearchFeeds(extraData.get("query").toString());
				break;
			default:
				break;
			}
		} catch (Exception e) {
			apiResponse.setSuccess(false);
			e.printStackTrace();
		}
		return apiResponse;
	}

	@Override
	protected void onPostExecute(ApiResponse result) {
		super.onPostExecute(result);

		switch (taskType) {

		case SEARCH:
			if (null != onPostExecute)
				onPostExecute.onPostExecute(result);
			break;
		default:
			break;
		}

		if (showLoader) {
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}

}
