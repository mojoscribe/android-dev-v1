package com.sudosaints.android.mojoscribe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.sudosaints.android.mojoscribe.adapter.DrawerOptionAdapter.OnOptionSelected;
import com.sudosaints.android.mojoscribe.util.Constants.DrawerOptionAction;
import com.sudosaints.android.mojoscribe.util.TypefaceLoader;
import com.sudosaints.android.mojoscribe.util.UIHelper;

public class AboutUsActivity extends Activity {

	Typeface typeface;
	TextView aboutUsTextView ;
	
	UIHelper uiHelper;
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
		setContentView(R.layout.about_us);
		uiHelper = new UIHelper(AboutUsActivity.this);
		uiHelper.generateDrawers(onOptionSelected, DrawerOptionAction.ABOUT_US, R.layout.about_us);
		typeface = TypefaceLoader.get(this, "fonts/SansRegular.ttf");
		
		aboutUsTextView=(TextView)findViewById(R.id.aboutUsTextView);
		aboutUsTextView.setTypeface(typeface);
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
		startActivity(new Intent(this, FeedsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
		finish();
	}
}
