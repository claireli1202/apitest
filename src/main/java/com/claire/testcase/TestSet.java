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
	

	//var2: config里的varialbles的固定值直接存，需要函数计算的，运行时计算好之后再存
	//eg: device_sn2
	//固定值 没有表达式
	private Map<String,Object> varsValue = new HashMap<String,Object>();
	
	//var3: config里func计算的值
	//eg: device_sn
	//var_name -> FuncObj 表达式
	private Map<String,FuncObj> varsFuncFormula = new HashMap<String,FuncObj>();
	

    public void Load(String path) {
//		load the yaml testset to a YamlCase object
		YamlCase yCase = YamlUtil.load(path);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    gson.toJson(yCase, System.out);
	    System.out.println();
//		get the variables in config
//		
		Map<String,Object> configVariables = yCase.getConfig().getVariables();
/* 处理config中variables的参数替换*/
		
		Utils.prepareVariables(configVariables,varsValue,varsFuncFormula);

//		使用正则表达式判断是否存在需要函数计算的variable，如果存在就调用Utils.prepareVariablesByFunc,得到varsByFunc，否则直接赋值给varsFixed
		
		
		
	}
	
	public void Run() {
		//
		
		for(String name : varsFuncFormula.keySet()) {
			FuncObj fObj = varsFuncFormula.get(name);
			try {
				varsValue.put(name, Utils.invokeFunc(fObj,varsValue));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		System.out.println("global variables: ");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(varsValue);
        System.out.println(json);
		
	}
	
	
	public static void main(String[] args) {
		TestSet ts = new TestSet();
		ts.Load("/home/claire/work/eclipse-workspace/apitest/src/test/resource/yaml/testset1.yaml");
		ts.Run();
		
	}
	
}
