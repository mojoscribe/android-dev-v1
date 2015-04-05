package com.sudosaints.android.mojoscribe.util;



import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;

import com.sudosaints.android.mojoscribe.exception.CommunicationException;
import com.sudosaints.android.mojoscribe.exception.ResponseFormatException;
import com.sudosaints.android.mojoscribe.util.ApiResponse.ApiError;

public class ResponseHelper {
	
	private static final int HTTP_STATUS_OK = 200;

	ObjectMapper mapper = new ObjectMapper();
	Context context;
	Logger logger;
	
	public ResponseHelper(Context ctx) {
		this.context = ctx;
		this.logger = new Logger(ctx);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private void checkResponse(ServerResponse response)
			throws CommunicationException, ResponseFormatException {
		if (null == response) {
			throw new CommunicationException("Error Communicating with Server");
		}
        		
		if (logger.debugEnabled) {
			dumpResponse(response);
		}
		
        if (!response.getContentType().contains("application/json")) {
			throw new ResponseFormatException(
					"Server Response not in JSON format");
		}
	}
	
	private void dumpResponse(ServerResponse response) {
		
		try {
			String resp = new String(response.getByteArray());
			logger.debug("Response Dump: "+resp);
			response.setResponseStream(IOUtils.toInputStream(resp));
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ApiResponse getApiResponse(ServerResponse response) {
		try {
			checkResponse(response);
		} catch (CommunicationException e1) {
			e1.printStackTrace();
			return new ApiResponse().setSuccess(false).setError(ApiError.COMMUNICATION_ERROR);
		} catch (ResponseFormatException e1) {
			e1.printStackTrace();
			return new ApiResponse().setSuccess(false).setError(ApiError.RESPONSE_ERROR);
		}

		ApiResponse apiResponse = null;
		try {
			apiResponse = new ApiResponse();
			
			Map<String, Object> responseMap = mapper.readValue(response.getResponseStream(), Map.class);			
			if(responseMap.containsKey("success")) {				
				apiResponse.setSuccess(Boolean.valueOf(responseMap.get("success")+""));
			} else {
				apiResponse.setSuccess(false);
			}
			
			if(apiResponse.isSuccess()) {
				apiResponse.setData(responseMap);
				
			} else {
				if ( null != responseMap.get("error") && !((Map<String,Object>) responseMap.get("error")).isEmpty()) {
					String errorResponseString =(String) ((Map<String,Object>) responseMap.get("error")).get("msg");
					int errorResponseInteger =Integer.parseInt("0"+((Map<String,Object>) responseMap.get("error")).get("code") + "");												
					apiResponse.setError(new ApiError(errorResponseInteger, errorResponseString));
				} else {
					return new ApiResponse().setSuccess(false).setError(ApiError.RESPONSE_ERROR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ApiResponse().setSuccess(false).setError(ApiError.GENERAL_ERROR);
		}
		return apiResponse;
	}
}
