package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.adapter.PollAdapter;
import com.sudosaints.android.mojoscribe.adapter.PollAdapter.OnPollSelected;
import com.sudosaints.android.mojoscribe.model.Poll;
import com.sudosaints.android.mojoscribe.model.PollAnswerOption;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class PollsActivity extends Activity {

	private UIHelper uiHelper;
	private List<Poll> polls;
	private ListView pollsListView;
	private PollAdapter pollAdapter;
	private OnOptionSelected onOptionSelected = new OnOptionSelected() {
		@Override
		public void onSelect() {
			uiHelper.leftDrawer.toggleMenu();
		}
	};
	private OnPollSelected onPollSelected = new OnPollSelected() {

		@Override
		public void onSelect(int pollId, int answerId) {
			// Toast.makeText(PollsActivity.this, pollId + " : " + answerId ,
			// Toast.LENGTH_SHORT).show();
			new SetPoll(PollsActivity.this, pollId, answerId).execute();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.polls_activity);
		uiHelper = new UIHelper(PollsActivity.this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.POLLS, R.layout.polls_activity);
		pollsListView = (ListView) findViewById(R.id.pollsListView);
		new GetPolls(this).execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}

	void displayPolls() {
		pollAdapter = new PollAdapter(PollsActivity.this, polls, onPollSelected);
		pollsListView.setAdapter(pollAdapter);
	}

	class GetPolls extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;

		public GetPolls(Context context) {
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
			ApiResponse apiResponse = apiRequestHelper.getpolls();
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
				List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
				polls = DataMapParser.getPolls(data);
				displayPolls();
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}

	class SetPoll extends AsyncTask<Void, Void, ApiResponse> {

		private Context context;
		private ProgressDialog progressDialog;
		int pollId, optionId;

		public SetPoll(Context context, int pollId, int optionId) {
			super();
			this.context = context;
			this.pollId = pollId;
			this.optionId = optionId;
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
			ApiResponse apiResponse = apiRequestHelper.setPoll(pollId, optionId);
			return apiResponse;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(ApiResponse result) {
			// TODO Auto-generated method stub
			List<PollAnswerOption> answerOptions;
			if (null != progressDialog && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			if (result.isSuccess()) {
				Map<String, Object> dataMap = (Map<String, Object>) result.getData();
				Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
				Poll poll = new Poll();
				answerOptions = DataMapParser.getPoll(data);
				for (Poll poll2 : polls) {
					if (poll2.getId() == pollId) {
						poll2.setAnswers(answerOptions);
					}
				}

				pollAdapter.refreshList();
			} else {
				uiHelper.CustomToast(result.getError().getMessage() + "");
				setResult(RESULT_CANCELED);
			}
		}

	}
}
