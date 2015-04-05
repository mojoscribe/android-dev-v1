package com.sudosaints.android.mojoscribe;

import java.util.List;
import java.util.Map;

import com.sudosaints.android.mojoscribe.model.IdValue;
import com.sudosaints.android.mojoscribe.model.UploadPost;

public class DataCache {
	private static boolean captureStart = false;
	private static Map<String, Object> dataMap;
	private static String q="";
	private static List<IdValue> idValueList;
	private static UploadPost uploadPost;
	
	public static boolean isCaptureStart() {
		return captureStart;
	}
	public static void setCaptureStart(boolean captureStart) {
		DataCache.captureStart = captureStart;
	}
	public static Map<String, Object> getDataMap() {
		return dataMap;
	}
	public static void setDataMap(Map<String, Object> dataMap) {
		DataCache.dataMap = dataMap;
	}
	public static List<IdValue> getIdValueList() {
		return idValueList;
	}
	public static void setIdValueList(List<IdValue> idValueList) {
		DataCache.idValueList = idValueList;
	}
	public static UploadPost getUploadPost() {
		return uploadPost;
	}
	public static void setUploadPost(UploadPost uploadPost) {
		DataCache.uploadPost = uploadPost;
	}


	public static String getQ() {
		return q;
	}

	public static void setQ(String q) {
		DataCache.q = q;
	}

}
