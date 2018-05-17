package com.claire.testcase;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import com.claire.tools.TestCase;

public class Test {
	private String URL = "";
	private String Method = "";
	private String name = "";
	private Map<String,Object> varsMerged = null;
	
	//var1: 本case固定定义的Variable值
	//eg: local_var4_1, local_var4_2
	//var_name -> var_value
	//固定值 没有表达式
	private Map<String,Object> varsValue = new HashMap<String, Object>();
	
	//var2: 本case里需要函数计算的Variable，存表达式，在运行是调用函数计算，存入varsValue
	//eg: device_sn
	//var_name -> FuncObj 表达式prepareVariables2
	private Map<String,FuncObj> varsFuncFormula = new HashMap<String, FuncObj>();
		
	
	//param_name -> param_value
	private Map<String, Object> paramValue = new HashMap<String, Object>();
	
	// param_name -> var_name
	private Map<String, String> paramVarFormula = new HashMap<String, String>();
	
	// param_name -> FuncObj
	private Map<String, FuncObj> paramFuncFormula = new HashMap<String, FuncObj>();
	
	//request
	private Map<String, Object> headerValue = new HashMap<String, Object>();
	private Map<String,FuncObj> headerFuncFormula = new HashMap<String, FuncObj>();
	
	//extract: 
	//variables extracted from testcase
	//var_name -> xpath/jsonpath 表达式
	private Map<String,String> extractFormula = null;
	
	public String getURL() {
		return this.URL;
	}
	
//	public void setVarsByFunc(Map<String, FuncObj> funcFormula) {
//		this.varsByFunc.clear();
//		
//		Map<String,Object> caseVariables = yCase.getConfig().getVariables();
////      处理config中variables的参数替换
//		Utils.prepareVariables(caseVariables,varsValue,varsFuncFormula);
//		
//		//计算本case local的变量
//		Utils.prepareVariablesByFunc(funcFormula, varsByFunc);
////		for(String varName : this.funcFormula.keySet()){
////			FuncObj varFn = this.funcFormula.get(varName);
////			Object varValue = Utils.invokeFunc(varFn);
////			if(varValue==null){
////				//TODO: throw exception
////			}
////			this.varsByFunc.put(varName, varValue);
////		}
//	}
	
	
	
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
	
	public void generateRuntimeVariables(Map<String, Object> globalVars){
		//计算本case的vars，与globalVars整合，保存到varsMerged
		
	}
	
	public String generateRuntimeURL() {
		//从varsMerged找到匹配的值，替换URL中的变量
		return "";
	}
	
	public Map<String, Object> generateRuntimeHeaders(){
//		for(String paramName : paramByFunc.keySet()){
//		FuncObj varFn= paramByFunc.get(paramName);
//		Object varValue = Utils.invokeFunc(varFn);
//		if(varValue==null){
//			//TODO: throw exception
//		}
//		params.put(paramName, varValue);
//	}	
		
		
		return null;
	}
	
	
	public Map<String, Object> generateRuntimeParams() {
//		//整合paramFixed + paramByVars + paramByFunc
//		//变成一个Map<String, Object>返回
//		//copy paramFixed and create new Map params
//		Map<String,Object> params = new HashMap<String,Object>(paramFixed);
//		
//		//loop the Map paramByVars and replace the variables
//		for(String paramName : paramByVars.keySet()){
//			String varName = paramByVars.get(paramName);
//			Object varValue = Utils.findVarValue(varName);
//			if(varValue==null){
//				//TODO: throw exception
//			}
//			params.put(paramName, varValue);
//		}
//
//		for(String paramName : paramByFunc.keySet()){
//			FuncObj varFn= paramByFunc.get(paramName);
//			Object varValue = Utils.invokeFunc(varFn);
//			if(varValue==null){
//				//TODO: throw exception
//			}
//			params.put(paramName, varValue);
//		}	
//		
//		return params;
		
		return null;
	}
	
	
	public void load(TestCase tc) {
		this.name = tc.getName();
		this.URL = tc.getRequest().getUrl();
		this.Method = tc.getRequest().getMethod();
		
//		get the variables in tc
		Map<String,Object> tcVariables = tc.getVariables();
//      处理tc中variables的参数替换
		Utils.prepareVariables(tcVariables, varsValue, varsFuncFormula);
		
        //处理tc.request.headers中variables的参数替换
		Map<String,Object> tcHeaders = tc.getRequest().getHeaders();
		Utils.prepareVariables(tcHeaders,headerValue,headerFuncFormula);
		
		//处理tc.request.params,分别保存
		Map<String,Object> tcParams = tc.getRequest().getParams();
		Utils.prepareVariables2(tcParams, paramValue, paramFuncFormula, paramVarFormula);
		
		//处理tc中的extract
		extractFormula = new HashMap<String, String>(tc.getExtract()); 
		
		
	}
	
	
	
}
