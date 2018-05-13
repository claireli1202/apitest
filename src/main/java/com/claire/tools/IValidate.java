package com.claire.tools;

import java.util.Map;

public class IValidate {
	private int statusCode;
	private String contentType;
	private Map<String, Object> responseBody = null;
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Map<String, Object> getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(Map<String, Object> responseBody) {
		this.responseBody = responseBody;
	}
}
