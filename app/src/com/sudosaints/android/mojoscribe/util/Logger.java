package com.sudosaints.android.mojoscribe.util;

import android.content.Context;
import android.util.Log;

public class Logger {

	private Context context;
	boolean debugEnabled;
	public static String tag = "MJS";
	
	/*public Logger(Context ctx, String tag) {
		this(ctx);
		this.tag = tag;
	}*/

	public Logger(Context ctx) {
		super();
		this.context = ctx;
        debugEnabled = true;
	}

	public void debug(String msg) {
		if (debugEnabled && msg!=null) {
			Log.d(tag, msg);
		}
	}

	public void debug(String msg, Throwable t) {
		if (debugEnabled && msg!=null) {
			Log.d(tag, msg, t);
		}
	}

	public void debug(Throwable t) {
		if (debugEnabled) {
			Log.d(tag, "Exception:", t);
		}
	}

	public void debug(String tag, String msg) {
		if (debugEnabled && msg!=null) {
			Log.d(tag, msg);
		}
	}
	
	public void warn(String msg) {
		Log.w(tag, msg);
	}

	public void info(String msg) {
		Log.i(tag, msg);
	}

	public void error(String msg) {
		Log.e(tag, msg);
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

}
