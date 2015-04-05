package com.sudosaints.android.mojoscribe.test;


import android.content.Context;

import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.util.ApiRequest;
import com.sudosaints.android.mojoscribe.util.ServerResponse;

public class MockServerResponseGenerator {

	Context context;
	
	public MockServerResponseGenerator(Context context) {
		this.context = context;
	}
	
	private int getMockDataFileResourceId(ApiRequest request) {
		switch (request.getRequestName()) {
		
		default:
			break;
		}
		
		return R.raw.responsemock_generror;
	}
	
	public ServerResponse getErrorMockResponse (ApiRequest request) {
		return new ServerResponse(400, "application/json", 
				context.getResources().openRawResource(getMockDataFileResourceId(request)));
	}
	
}
