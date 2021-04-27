package com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


public class HttpRequest {

	public static String doPostForHttps(String suffixUrl, String jsonstr, String charset, String key) {
		if (null == charset) {
			charset = "utf-8";
		}
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;

		String url = suffixUrl;

		try {
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Authorization", key);
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("SSL POST Error, message is " + ex.getMessage());
		}
		return result;
	}

	public static String doGetForHttps(String suffixUrl, String jsonstr, String charset, String key) {
		if (null == charset) {
			charset = "utf-8";
		}
		HttpClient httpClient = null;
		HttpGet httpGet = null;
		String result = null;

		String url = suffixUrl;

		try {
			httpClient = new SSLClient();
			httpGet = new HttpGet(url);
			httpGet.addHeader("Content-Type", "application/json");
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Authorization", key);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("SSL GET Error, message is " + e.getMessage());
		}
		return result;
	}
	
	public static String doPutForHttps(String url, String jsonstr, String charset, String key) {
		if (null == charset) {
			charset = "utf-8";
		}
		HttpClient httpClient = null;
		HttpPut httpput = null;
		String result = null;

		try {
			httpClient = new SSLClient();
			httpput = new HttpPut(url);
			httpput.addHeader("Content-Type", "application/json");
			httpput.setHeader("Accept", "application/json");
			httpput.setHeader("Authorization", key);
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
			httpput.setEntity(se);
			HttpResponse response = httpClient.execute(httpput);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("SSL PUT Error, message is " + e.getMessage());
		}
		return result;
	}
	
	public static String doPatchForHttps(String url, String jsonstr, String charset, String key) {
		if (null == charset) {
			charset = "utf-8";
		}
		HttpClient httpClient = null;
		HttpPatch httppatch = null;
		String result = null;

		try {
			httpClient = new SSLClient();
			httppatch = new HttpPatch(url);
			httppatch.addHeader("Content-Type", "application/json");
			httppatch.setHeader("Accept", "application/json");
			httppatch.setHeader("Authorization", key);
			StringEntity se = new StringEntity(jsonstr);
			se.setContentType("text/json");
			se.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
			httppatch.setEntity(se);
			HttpResponse response = httpClient.execute(httppatch);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("SSL PUT Error, message is " + e.getMessage());
		}
		return result;
	}

}
