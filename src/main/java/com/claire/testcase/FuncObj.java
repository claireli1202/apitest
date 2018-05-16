package com.claire.testcase;

import java.util.List;

public class FuncObj {
	private String funcName = "";
	private List<String> funcParams = null;
	
	public FuncObj(String funcName, List<String> funcParams) {
		this.funcName = funcName;
		this.funcParams = funcParams;
	}
	
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public List<String> getFuncParams() {
		return funcParams;
	}
	public void setFuncParams(List<String> funcParams) {
		this.funcParams = funcParams;
	}
	
	

}
