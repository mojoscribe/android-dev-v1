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

import com.sudosaints.android.mojoscribe.adapter.CategoriesOptionAdapter;
import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.util.ApiRequestHelper;
import com.sudosaints.android.mojoscribe.util.ApiResponse;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.DataMapParser;
import com.sudosaints.android.mojoscribe.util.UIHelper;
import com.sudosaints.android.mojoscribe.view.LoadingDialog;

public class CategoriesListActivity extends Activity{

	UIHelper uiHelper;
	List<IdValue> categoriesList;
	ListView categoriesListView;
	CategoriesOptionAdapter adapter;
 	
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
		setContentView(R.layout.categories_list_activity);
		
		uiHelper=new UIHelper(CategoriesListActivity.this);
		
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.CATEGOIES, R.layout.categories_list_activity);
		
		categoriesListView=(ListView)findViewById(R.id.categoriesActivityListView);
		
		new getCategories(CategoriesListActivity.this).execute();
		
		}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResumeActivity();
	}
	
	void displayCategoriesList(){
		adapter= new CategoriesOptionAdapter(CategoriesListActivity.this, categoriesList);
		categoriesListView.setAdapter(adapter);
	}
	
	class getCategories extends AsyncTask<Void, Void, ApiResponse>{


		private Context context;
		private ProgressDialog progressDialog;

		public getCategories(Context context) {
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
			ApiResponse apiResponse = apiRequestHelper.getCategories();
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
				categoriesList= DataMapParser.getcategories(data);
				displayCategoriesList();
			} else {
				uiHelper.CustomToast( result.getError().getMessage()+"");
				setResult(RESULT_CANCELED);
				finish();
			}
		}

	
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}
}
