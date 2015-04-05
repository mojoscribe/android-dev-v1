package com.sudosaints.android.mojoscribe.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sudosaints.android.mojoscribe.Preferences;
import com.sudosaints.android.mojoscribe.R;
import com.sudosaints.android.mojoscribe.exception.CommunicationException;
import com.sudosaints.android.mojoscribe.test.MockServerResponseGenerator;
import com.sudosaints.android.mojoscribe.util.ApiRequest.PostFile;
import com.sudosaints.android.mojoscribe.util.ApiRequest.RequestMethod;
import com.sudosaints.android.mojoscribe.util.ApiResponse.ApiError;


public class HttpHelper {

	private DefaultHttpClient client;
	private Logger logger;
	Context ctx;
	Preferences prefs;

	private boolean isMockEnabled() {
		return ctx.getResources().getBoolean(R.bool.isMockEnabled);
	}

	public static ServerResponse doSimpleHttpRequest(String uri) throws CommunicationException {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(uri));
			HttpResponse response = client.execute(request);
			String contentType = response.getEntity().getContentType().getValue().trim();
			return new ServerResponse(response.getStatusLine().getStatusCode(), contentType, response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommunicationException(e);
		}
	}

	public HttpHelper(Context ctx) {
		this.client = getHttpClient();
		logger = new Logger(ctx);
		this.ctx = ctx;
		this.prefs = new Preferences(ctx);
	}

	private DefaultHttpClient getHttpClient() {

		DefaultHttpClient lclient = new DefaultHttpClient();
		ClientConnectionManager mgr = lclient.getConnectionManager();
		HttpParams params = lclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 16000);

		lclient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);

		return lclient;
	}

	private HttpResponse doHttpRequest(ApiRequest apiRequest, boolean useNewClient) throws CommunicationException {

		DefaultHttpClient httpClient;
		httpClient = useNewClient ? getHttpClient() : client;
		Properties reqParams = apiRequest.getReqParams();

		HttpUriRequest request = null;
		String url = apiRequest.isUrlAbsolute() ? apiRequest.getUrl() : prefs.getServerUrl() + apiRequest.getUrl();

		logger.debug("API HTTP REQUEST: " + url);
		logger.debug("Request Parameters: " + reqParams);

		List<NameValuePair> nameValuePairs = null;
		if (reqParams != null && reqParams.size() > 0) {
			nameValuePairs = new ArrayList<NameValuePair>(reqParams.size());
			for (Object pname : reqParams.keySet()) {
				nameValuePairs.add(new BasicNameValuePair((String) pname, reqParams.getProperty((String) pname)));
			}
		}
		 if (apiRequest.getRequestMethod() == RequestMethod.POST) {
			request = new HttpPost(url);
			if (nameValuePairs != null) {
				try {
					((HttpPost) request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new CommunicationException(e);
				}
			}
		} else if (apiRequest.getRequestMethod() == RequestMethod.POST_RAW) {
			request = new HttpPost(url);
			FileEntity entity = new FileEntity(apiRequest.getPostFile(), "application/octet-stream");
			((HttpPost) request).setEntity(entity);
		} else if (apiRequest.getRequestMethod() == RequestMethod.FILE_UPLOAD) {

			request = new HttpPost(url);
			MultipartEntity form = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			httpClient.getParams().getBooleanParameter("http.protocol.expect-continue", true);
	//changes done by indraneel	
			for(PostFile postFile : apiRequest.getPostFiles()){
				FileBody fileBody = new FileBody(postFile.getPostFile());
				form.addPart(postFile.getFileName(), fileBody);
			}
			
		//	FileBody fileBody = new FileBody(apiRequest.getPostFile());
		//	form.addPart(apiRequest.getPostFileAttrName(), fileBody);
			try {
				for (NameValuePair nameValuePair : nameValuePairs) {
					form.addPart(nameValuePair.getName(), new StringBody(nameValuePair.getValue()));
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new CommunicationException(e);
			}
			((HttpEntityEnclosingRequestBase) request).setEntity(form);
		} else if (apiRequest.getRequestMethod() == RequestMethod.PUT) {

			request = new HttpPut(url);
			if (nameValuePairs != null) {
				try {
					((HttpPut) request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new CommunicationException(e);
				}
			}
		} else if (apiRequest.getRequestMethod() == RequestMethod.DELETE) {

			request = new HttpDelete(url);
		} else { // GET
			if (nameValuePairs != null && !apiRequest.isUrlAbsolute()) {
				try {
					// String serverName =
					// PreferenceManager.getDefaultSharedPreferences(ctx).getString(Preferences.serverName,
					// Constants.SERVERNAME);
					// String serverName =
					// CommonUtil.getServerNameFromURL(ctx.getResources().getString(R.string.server_url));
					// String nodeName =
					// CommonUtil.getNodeNameFromURL(ctx.getResources().getString(R.string.server_url));
					String serverName = CommonUtil.getServerNameFromURL(prefs.getServerUrl());
					String nodeName = CommonUtil.getNodeNameFromURL(prefs.getServerUrl());

					logger.debug("Server Name - " + serverName);
					logger.debug("Node Name - " + nodeName);
					URI uri = URIUtils.createURI(Constants.URI_SCHEME, serverName, -1, nodeName + "/" + apiRequest.getUrl(), URLEncodedUtils.format(nameValuePairs, "UTF-8"), null);
					logger.debug("URI - " + uri.toString());
					request = new HttpGet(uri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
					throw new CommunicationException(e);
				}
			} else {
				try {
					request = new HttpGet(url);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new CommunicationException(e);
				}
			}
		}

		boolean execOK = false;
		HttpResponse response = null;

		logger.debug("Adding auth-key to headers");
		request.setHeader("api-key", ctx.getResources().getString(R.string.api_key));
		if (apiRequest.isAddAccessTokenHeaders()) {
			logger.debug("Adding auth-token to headers");
			request.setHeader("auth-token", prefs.getAccessToken());
		}

		try {
			response = httpClient.execute(request);
			execOK = true;
		} catch (Exception e) {
			logger.warn("HttpClient->execute() threw exception, creating new instance of HttpClient and retrying. Exception: " + e.getMessage());
			e.printStackTrace();
		}

		if (!execOK && !useNewClient) {
			httpClient = getHttpClient();
			try {
				response = httpClient.execute(request);
			} catch (Exception e) {
				// We still have a problem. Bail out.
				logger.warn("HttpClient->execute() AGAIN threw exception. Bailing out.. Stack Trace follows:");
				e.printStackTrace();
			}
		}
		return response;
	}

	public ServerResponse sendRequest(ApiRequest apiRequest) throws CommunicationException {
		return sendRequest(apiRequest, false);
	}

	public ServerResponse sendRequest(ApiRequest apiRequest, boolean useNewClient) throws CommunicationException {

		if (isMockEnabled()) {
			return new MockServerResponseGenerator(ctx).getErrorMockResponse(apiRequest);
		}

		HttpEntity entity = null;
		HttpResponse response = null;

		response = doHttpRequest(apiRequest, useNewClient);
		if (response == null) {
			throw new CommunicationException("Server Communication Error");
		}

		entity = response.getEntity();

		if (entity != null) {
			// byte[] data = EntityUtils.toByteArray(entity);
			if (entity.getContentType() == null) {
				throw new CommunicationException(ApiError.MISSING_CONTENT_TYPE_HEADER);
			}
			String contentType = entity.getContentType().getValue().trim();
			try {
				Header contentEncoding = response.getFirstHeader("Content-Encoding");
				if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					InputStream inputStream = new GZIPInputStream(entity.getContent());
					logger.debug("Content Encoding is Gzip");
					return new ServerResponse(response.getStatusLine().getStatusCode(), contentType, inputStream);
				} else {
					logger.debug("Content Encoding is NOT Gzip");
					return new ServerResponse(response.getStatusLine().getStatusCode(), contentType, entity.getContent());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CommunicationException(e);
			}
		} else if (!(apiRequest.getRequestMethod() == RequestMethod.DELETE)) {
			throw new CommunicationException("Communication Error");
		} else {
			return null;
		}

	}

	public class HttpPatch extends HttpEntityEnclosingRequestBase {

		public final static String METHOD_NAME = "PATCH";

		public HttpPatch() {
			super();
		}

		public HttpPatch(final URI uri) {
			super();
			setURI(uri);
		}

		public HttpPatch(final String uri) {
			super();
			setURI(URI.create(uri));
		}

		@Override
		public String getMethod() {
			return METHOD_NAME;
		}

	}

	public Bitmap downloadImage(String url) {

		byte[] data = downloadRaw(url);
		if (null == data)
			return null;

		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		return bm;
	}

	/**
	 * Read response as raw bytes (typically for images)
	 * 
	 * @param url
	 * @param useAuth
	 * @return
	 */
	public byte[] downloadRaw(String url) {
		byte[] data = null;
		ServerResponse response = null;
		// app.getLogger().debug("Fetching Image: "+url);
		try {
			response = sendRequest(new ApiRequest(false).setUrl(url).setUrlAbsolute(true).setRequestMethod(RequestMethod.GET), true);
			data = response.getByteArray();
		} catch (CommunicationException e) {
			logger.warn("Image Download Failed for " + url);
			e.printStackTrace();
			return null;
		}
		if (null == data)
			return null;

		if (response.getContentType().startsWith("text")) {
			logger.warn("Received non-binary data when expecting binary!");
			// app.getLogger().debug(new String(data));
			return null;
		} else {
			return data;
		}

	}

}
