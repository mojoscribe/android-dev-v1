package com.sudosaints.android.mojoscribe.db;

import android.content.Context;

public class DBUtils {
	
	Context context;
	DBHelper dbHelper;
	
	public DBUtils(Context context) {
		super();
		this.context = context;
	}
	
	public DBHelper getDBHelper() {

		if(dbHelper != null && dbHelper.isDbOpened()) {
			return dbHelper;
		}
		dbHelper = new DBHelper(context);
		dbHelper.open();
		return dbHelper;
	}
}
