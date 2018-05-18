package com.claire.testcase;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.claire.tools.TestCase;

public class Test {
	private String URL = "";
	private String Method = "";
	private String name = "";
	private Map<String,Object> varsMerged = null;
	private static final String regexUrl = "\\$([\\w]+)";
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
	
	//request: 
	//user_agent:1
	private Map<String, Object> headerValue = new HashMap<String, Object>();
	//user_agent: $user_agent
	private Map<String, String> headerVarFormula = new HashMap<String, String>();
	//user_agent: ${genRandomString($aa)}
	private Map<String,FuncObj> headerFuncFormula = new HashMap<String, FuncObj>();
	
	//extract: 
	//variables extracted from testcase
	//var_name -> xpath/jsonpath 表达式
	private Map<String,String> extractFormula = null;
	
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
		//计算本case中所有需要函数计算的variables，结果存入varsValue
		//函数的参数可以是变量引用，但是只可能引用config里的全局变量
		for(String name : varsFuncFormula.keySet()) {
			FuncObj fObj = varsFuncFormula.get(name);
			try {
				varsValue.put(name, Utils.invokeFunc(fObj, globalVars));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	
		//将varsValue与globalVars整合，保存到varsMerged
		varsMerged = new HashMap<String, Object>();
		varsMerged.putAll(globalVars);
		// varsValue中含有和globalVars中相同的key，那么执行如下方法之后
        // varsValue中的值会覆盖掉globalVars中的值
		varsMerged.putAll(varsValue);
	}
	
	public String generateRuntimeURL() throws Exception {
		//从varsMerged找到匹配的值，替换URL中的变量
		Pattern pattern = Pattern.compile(regexUrl);
		Matcher matcher = pattern.matcher(this.URL);
		List<String> urlVars = new ArrayList<String>();
		String runtimeURL = this.URL;
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		while(matcher.find()){
			urlVars.add(matcher.group(1));
			System.out.println("URL vars:" + matcher.group(1));
		}
		
		System.out.println("URL vars size: " + urlVars.size());
		
		for(String var : urlVars) {
			String varValue = Utils.findVarValue(var, this.varsMerged).toString();
			if(varValue == null) {
				String ex = String.format("URL(%s)引用的%s未找到定义", this.URL, var);
				throw new Exception(ex);
			}
			//如果URL中引用的变量找到了对应的变量定义，替换
//			System.out.printf("Begin replace: %s %s %s", runtimeURL, varValue);
			
			runtimeURL = runtimeURL.replace("$"+var, varValue);
			
		}
		
		return runtimeURL;
	}
	
	public Map<String, Object> generateRuntimeHeaders() throws Exception{
		//替换headerVarFormula中的变量引用,结果存入headerValue
		for(String k : this.headerVarFormula.keySet()) {
			String v = this.headerVarFormula.get(k);
			Object value = Utils.findVarValue(v, this.varsMerged);
			if(value == null) {
				String ex = String.format("header%s引用的%s未找到定义", k, v);
				throw new Exception(ex);
			}
			this.headerValue.put(k, value);
		}		
		
		//计算本case中所有需要函数计算的header，结果存入headerValue
		for(String name : this.headerFuncFormula.keySet()) {
			FuncObj fObj = headerFuncFormula.get(name);
			try {
				headerValue.put(name, Utils.invokeFunc(fObj,varsMerged));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return headerValue;
	}
	
	
	public Map<String, Object> generateRuntimeParams() throws Exception {
		//替换paramVarFormula中的变量引用,结果存入paramValue
		for(String k : this.paramVarFormula.keySet()) {
			String v = this.paramVarFormula.get(k);
			Object value = Utils.findVarValue(v, this.varsMerged);
			if(value == null) {
				String ex = String.format("params%s引用的%s未找到定义", k, v);
				throw new Exception(ex);
			}
			this.paramValue.put(k, value);
		}		
		
		//计算本case中所有需要函数计算的param，结果存入paramValue
		for(String name : this.paramFuncFormula.keySet()) {
			FuncObj fObj = paramFuncFormula.get(name);
			try {
				paramValue.put(name, Utils.invokeFunc(fObj,varsMerged));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return paramValue;
	}
	
	
	public void load(TestCase tc) {
		this.name = tc.getName();
		this.URL = tc.getRequest().getUrl();
		this.Method = tc.getRequest().getMethod();
		
		//get the variables in tc
		Map<String,Object> tcVariables = tc.getVariables();
		
        //处理tc中variables的参数替换
		Utils.prepareVariables(tcVariables, varsValue, varsFuncFormula);
		
        //处理tc.request.headers中variables的参数替换
		Map<String,Object> tcHeaders = tc.getRequest().getHeaders();
		Utils.prepareVariables2(tcHeaders, headerValue, headerFuncFormula, headerVarFormula);
		
		//处理tc.request.params,分别保存
		Map<String,Object> tcParams = tc.getRequest().getParams();
		Utils.prepareVariables2(tcParams, paramValue, paramFuncFormula, paramVarFormula);
		
		//处理tc中的extract
		extractFormula = new HashMap<String, String>(tc.getExtract()); 
		
		
	}
	
	
	
}
