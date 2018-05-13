package com.claire.tools;

import java.util.Map;

public class TestCase {
	private IRequest request;
	private IValidate validate;
	private Map<String,String> extract = null;
	
	public Map<String, String> getExtract() {
		return extract;
	}
	public void setExtract(Map<String, String> extract) {
		this.extract = extract;
	}
	public IRequest getRequest() {
		return request;
	}
	public void setRequest(IRequest request) {
		this.request = request;
	}
	public IValidate getValidate() {
		return validate;
	}
	public void setValidate(IValidate validate) {
		this.validate = validate;
	}
	

	
}
