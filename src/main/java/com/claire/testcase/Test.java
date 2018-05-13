package com.claire.testcase;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class Test {
	//variables extracted from testcase
	//var_name -> xpath/jsonpath 表达式
	private Map<String,String> extractFormula;

	//var4: local fixed 代表本case固定定义的值
	//eg: local_var4_1, local_var4_2
	//var_name -> var_value
	private Map<String, Object> varsFixed;
	
	//var5: local with function calc 的需要函数计算的值
	//eg: local_var5
	//var_name -> func_obj
//	private Map<String, FuncObj> funcFormula;
	private Map<String, Object> varsByFunc;
	
	
	private String URL = "";
	private String Method = "";	
	// params指发包时候的包体param
	
	//类型A： params 固定的 
	//eg：param1,param2
	//param_name -> param_value
	private Map<String, Object> paramFixed;
	
	//params replace with variables(var1 + var2 + var3 + var4 + var5) 
	//eg: param3,param4,param5,param6,param7
	// param_name -> var_name
	private Map<String, String> paramByVars;
	
	//params replace with function calc 
	//eg: param8
	private Map<String, FuncObj> paramByFunc;
	
	private Map<String, Object> headerFixed;
	private Map<String, String> headerByVars;
	private Map<String, FuncObj> headerByFunc;
	
	
	public String getURL() {
		return this.URL;
	}
	
	public void setVarsByFunc(Map<String, FuncObj> funcFormula) {
		this.varsByFunc.clear();
		//计算本case local的变量
		Utils.prepareVariablesByFunc(funcFormula, varsByFunc);
//		for(String varName : this.funcFormula.keySet()){
//			FuncObj varFn = this.funcFormula.get(varName);
//			Object varValue = Utils.invokeFunc(varFn);
//			if(varValue==null){
//				//TODO: throw exception
//			}
//			this.varsByFunc.put(varName, varValue);
//		}
	}
	
	public Map<String, Object> getVarsByFunc() {
		return varsByFunc;
	}
	
	public boolean validate(Object response) {
		//校验结果
		return true;
	}
	
	public Map<String,Object> extractVariables(Response response) {
		Map<String,Object> values = new HashMap<String,Object>();
		for(String name : extractFormula.keySet()){
			//json path to be extracted from the response
			String path = extractFormula.get(name);
			//extract from response
			//response.getBody().
			Object value = null;
			values.put(name, value);
		}
		return values;
	}
	
	
	public Map<String, Object> generateRuntimeParams() {
		//整合paramFixed + paramByVars + paramByFunc
		//变成一个Map<String, Object>返回
		//copy paramFixed and create new Map params
		Map<String,Object> params = new HashMap<String,Object>(paramFixed);
		
		//loop the Map paramByVars and replace the variables
		for(String paramName : paramByVars.keySet()){
			String varName = paramByVars.get(paramName);
			Object varValue = Utils.findVarValue(varName);
			if(varValue==null){
				//TODO: throw exception
			}
			params.put(paramName, varValue);
		}

		for(String paramName : paramByFunc.keySet()){
			FuncObj varFn= paramByFunc.get(paramName);
			Object varValue = Utils.invokeFunc(varFn);
			if(varValue==null){
				//TODO: throw exception
			}
			params.put(paramName, varValue);
		}	
		
		return params;
	}
	
	public Map<String, Object> generateRuntimeHeader() {
		//整合headerFixed + headerByVars + headerByFunc
		//变成一个Map<String, Object>返回
		return null;
	}
	
//	private Object findVarValue(String varName) {
//		//第一步 先取全局里的
//		//第二步 取本case里的，同名的覆盖
//		return null;
//	}
//	
//	private Object invokeFunc(FuncObj fn) {
//		//call findVarValue if there's any params need to be replaced by variable
//		return null;
//		
//	}
}
