package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.StringTokenizer;

import com.sudosaints.android.mojoscribe.util.IntentExtras;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class WebActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			//http://mojo.com/single/milk-35
			Intent intent = getIntent();
		    String action = intent.getAction();
		    Uri data = intent.getData();
		    String scheme = data.getScheme(); // "http"
		    String host = data.getHost(); // "mojo.com"
		    List<String> params = data.getPathSegments();
		    String first = params.get(0); // "single"
		    String second = params.get(1); // "milk-35"
		    String slugData=params.get(params.size()-1);
		    
		    StringTokenizer tokenizer = new StringTokenizer(slugData, "-");
		    String idString="0";
		    while(tokenizer.hasMoreTokens()){
		    	idString=tokenizer.nextToken();
		    }
		    Intent intent1 = new Intent(WebActivity.this, SingleFeedActivity.class);
			intent1.putExtra(IntentExtras.INTENT_FEED_ID, Integer.parseInt(idString));
			startActivity(intent1);
		    finish();
		    
		    
		} catch (Exception e) {
			// TODO: handle exception
			finish();
		}
	}
}
