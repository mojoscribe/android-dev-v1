package com.sudosaints.android.mojoscribe.util;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class DataHelper {

	private Context context;

	private CommonUtil commonUtil;

	public DataHelper(Context context) {
		super();
		this.context = context;
		this.commonUtil = new CommonUtil(this.context);
	}

	public void createDataDirIfNotExists() {
		if (isExtStorageAvailable()) {
			File root = Environment.getExternalStorageDirectory();
			String path = root.getAbsolutePath() + "/MojoScribe/data/";
			File dataDir = new File(path);
			if (!(dataDir.exists() && dataDir.isDirectory())) {
				dataDir.mkdirs();
			}
		}
	}

	public String getDataDir() {
		File root = Environment.getExternalStorageDirectory();
		String path = root.getAbsolutePath() + "/MojoScribe/data";
		return path;
	}

	public static boolean isExtStorageAvailable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	public String getRealPathFromURI(Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Video.VideoColumns.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}

	public boolean inputDataFilesExist() {
		if (isExtStorageAvailable()) {
			File root = Environment.getExternalStorageDirectory();
			String path = root.getAbsolutePath() + "/MojoScribe/input/";
			File sessionFile = new File(path);
			File userFile = new File(path);
			File statusFile = new File(path);
			if (sessionFile.exists() && userFile.exists() && statusFile.exists()) {
				return true;
			}
		}

		return false;
	}

	public boolean checkFiles() {
		if (!isExtStorageAvailable()) {
			return false;
		}

		File root = Environment.getExternalStorageDirectory();
		String path = root.getAbsolutePath() + "/MojoScribe/output/";
		File outputDir = new File(path);
		if (outputDir.list().length == 0) {
			return false;
		}
		return true;
	}
	
	
}
