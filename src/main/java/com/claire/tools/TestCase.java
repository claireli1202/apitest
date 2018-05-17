package com.claire.tools;

import java.util.Map;

public class TestCase {
	private String name;
	private IRequest request;
	private Map<String, Object> variables;
	private IValidate validate;
	private Map<String,String> extract;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
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
