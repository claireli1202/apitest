package com.claire.testcase;

import java.util.HashMap;
import java.util.Map;

import com.claire.tools.IConfig;
import com.claire.tools.YamlCase;
import com.claire.tools.YamlUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestSet {
	

	public String base_url = "";
//	private Map<String,String> headers = null;
	
	
	//var1: extract的变量,可以被所有的Test使用和修改
	//eg: token: content.token
	private Map<String,Object> varsByExtracted = new HashMap<String,Object>();
	

	//var2: config里的varialbles的固定值
	//eg: device_sn2
	//固定值 没有表达式
	private Map<String,Object> varsFixed = new HashMap<String,Object>();
	
	//var3: config里func计算的值
	//eg: device_sn
	//var_name -> FuncObj 表达式
//	private Map<String,FuncObj> funcFormula;
	private Map<String,Object> varsByFunc = new HashMap<String,Object>();
	
	public Map<String,Object> getVarsByExtracted() {
		return varsByExtracted;
	}
	
	public void setVarsByExtracted(Map<String,Object> values){
		for(String name : values.keySet()){
			varsByExtracted.put(name, values.get(name));
		}
	}
	
	public Map<String,Object> getVarsFixed(){
		return varsFixed;
	}
	
	public void setVarsFixed(Map<String,Object> values) {
		for(String name : values.keySet()){
			varsFixed.put(name, values.get(name));
		}
	}
	
	public Map<String,Object> getVarsByFunc() {
		return varsByFunc;
	}
	
	public void setVarsByFunc(Map<String,FuncObj> funcFormula) {
		this.varsByFunc.clear();
		Utils.prepareVariablesByFunc(funcFormula, this.varsByFunc);
	}

	public void Load(String path) {
//		load the yaml testset to a YamlCase object
		YamlCase yCase = YamlUtil.load(path);
		Gson gson = new GsonBuilder().create();
	    gson.toJson(yCase, System.out);
	    System.out.println();
//		get the variables in config
//		
		Map<String,Object> configVariables = yCase.getConfig().getVariables();
/* 处理config中variables的参数替换*/
		
		Utils.prepareVariables(configVariables,varsFixed,varsByFunc);
//		判断是否存在固定值的config variable
		if (configVariables == null) {
//			log: no config variables in the test set
		}
//		使用正则表达式判断是否存在需要函数计算的variable，如果存在就调用Utils.prepareVariablesByFunc,得到varsByFunc，否则直接赋值给varsFixed
		
		
		
	}
	
	public void Run() {
		//
		
		
	}
	
	
	public static void main(String[] args) {
		TestSet ts = new TestSet();
		ts.Load("/home/claire/work/eclipse-workspace/apitest/src/test/resource/yaml/testset1.yaml");
		
		
	}
	
}
