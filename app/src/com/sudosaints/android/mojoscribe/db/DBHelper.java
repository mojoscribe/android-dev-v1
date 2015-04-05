package com.sudosaints.android.mojoscribe.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import com.sudosaints.android.mojoscribe.util.Constants;
import com.sudosaints.android.mojoscribe.util.Logger;

public class DBHelper {

	public static String POST_TABLE_NAME = "PostTable";

	/** POST Table Keys **/
	public static String KEY_POST_ID = "POST_ID";
	/** End **/

	private static String CREATE_POST_TABLE_QUERY = "CREATE TABLE " + POST_TABLE_NAME + " ( " + KEY_POST_ID + " INTEGER PRIMARY KEY " + ");";

	private static String DROP_POST_TABLE_QUERY = "DROP TABLE " + POST_TABLE_NAME + " IF EXIST;";

	private SQLiteDatabase database;
	private DatabaseHelper databaseHelper;
	private Context context;
	private Logger logger;

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_POST_TABLE_QUERY);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_POST_TABLE_QUERY);
			onCreate(db);
		}

	}

	public DBHelper(Context context) {
		this.context = context;
		logger = new Logger(this.context);
	}

	public DBHelper open() throws SQLException {
		databaseHelper = new DatabaseHelper(context, Constants.DATABASE_NAME, null, 1);
		database = databaseHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		databaseHelper.close();
		if (null != database && database.isOpen()) {
			logger.debug("Closing database");
			database.close();
		}
	}

	public boolean isDbOpened() {
		if (null != database && database.isOpen()) {
			return true;
		}
		return false;
	}

	public void emptyAllData() {
		database.delete(POST_TABLE_NAME, null, null);
	}

	public boolean savePostId(int postId) {
		try {
			ContentValues values = new ContentValues();
			values.put(KEY_POST_ID, postId);

			long result = database.insert(POST_TABLE_NAME, null, values);
			if (result > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	public boolean isPostId(int postId) {
		try {
			Cursor cursor = null;
			String query = "SELECT " + KEY_POST_ID + " FROM " + POST_TABLE_NAME + " WHERE " + KEY_POST_ID + "=" + postId;
			cursor = database.rawQuery(query, null);
			if (cursor.moveToFirst())
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

}