package com.claire.tools;

import java.util.Map;

public class IConfig {
	private String name;
	private Map<String,Object> variables;
	private IRequest request;
	
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
	public IRequest getRequest() {
		return request;
	}
	public void setRequest(IRequest request) {
		this.request = request;
	} 
	
	



	
	
	
}
