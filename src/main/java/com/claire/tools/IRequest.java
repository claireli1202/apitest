package com.claire.tools;

import java.util.Map;

public class IRequest {
	private String path;
	private String method;
	private Map<String,Object> params = null;

	
//	private Map<String,Object> formParams = null;
//	private Map<String,Object> queryParams = null;
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	
	
}
