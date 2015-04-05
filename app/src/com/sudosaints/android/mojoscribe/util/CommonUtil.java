package com.sudosaints.android.mojoscribe.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.Toast;

import com.sudosaints.android.mojoscribe.model.IdValue;

public class CommonUtil {

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	Context context;

	public CommonUtil(Context context) {
		super();
		this.context = context;
	}

	public boolean isDevBuild() {
		// return
		// context.getResources().getString(R.string.build_type).startsWith("dev");
		return true;
	}

	public static String getServerNameFromURL(String url) {
		try {
			int from = url.indexOf("//") + 2;
			int to = url.indexOf("/", from);
			if (to < 0) {
				return url.substring(from);
			}
			return url.substring(from, to);
		} catch (Exception e) {
			e.printStackTrace();
			return url;
		}

	}

	public static String getNodeNameFromURL(String url) {
		try {
			int firstIndex = url.indexOf("//");
			int from = url.indexOf("/", firstIndex + 2);
			if (url.charAt(from - 1) == '/') {
				return "";
			}
			int to = url.length();
			return url.substring(from, to);
		} catch (Exception e) {
			e.printStackTrace();
			return url;
		}
	}

	public static String removeTrailingSlashes(String str) {
		while (str.endsWith("/") && str.length() > 1) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static boolean validateEmail(String email) {
		Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = emailPattern.matcher(email);
		return matcher.matches();
	}

	public static String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		activity.startManagingCursor(cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static String doListIntToString(List<Integer> integersList) {

		String string = "";
		for (int i : integersList) {
			string += String.valueOf(i) + ",";
		}

		return string;
	}

	public static String[] getValue(List<IdValue> list) {
		List<String> returnList = new ArrayList<String>();
		for (IdValue item : list) {
			returnList.add(item.getName() + "");
		}
		return ((String[]) returnList.toArray(new String[0]));
	}

	public static String getDecimalFormat(float number) {
		number *= 10;
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		return decimalFormat.format(number);
	}

	public static SpannableString doSpannableString(String stringVal, Typeface typeface) {
		SpannableString spannableString = new SpannableString(stringVal);

		spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, stringVal.length(), 0);
		spannableString.setSpan(new RelativeSizeSpan(1.3f), 0, stringVal.length(), 0);
		return spannableString;
	}

}
