package com.sudosaints.android.mojoscribe.exception;



@SuppressWarnings("serial")
public class ResponseFormatException extends Exception {

	public ResponseFormatException(Throwable e) {
		super(e);
	}
	
	public ResponseFormatException(String msg) {
		super(msg);
	}

}
