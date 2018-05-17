package com.claire.testcase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.claire.tools.IConfig;
import com.claire.tools.TestCase;
import com.claire.tools.YamlCase;
import com.claire.tools.YamlUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestSet {
	

	public String name = "";
//	private Map<String,String> headers = null;	

	//var2: config里的varialbles的固定值直接存:需要函数计算的，运行时计算好之后再存:extract的变量,可以被所有的Test使用和修改
	//eg: device_sn2
	//eg: token: content.token
	//固定值 没有表达式
	private Map<String,Object> varsValue = new HashMap<String,Object>();
	
	//var3: config里func计算的值
	//eg: device_sn
	//var_name -> FuncObj 表达式
	private Map<String,FuncObj> varsFuncFormula = new HashMap<String,FuncObj>();
	
	//request
	private String baseUrl = "";
	private Map<String, Object> headerValue = new HashMap<String,Object>();
	private Map<String,FuncObj> headerFuncFormula = new HashMap<String,FuncObj>();
	
	private List<Test> tests = new ArrayList<Test>(); 

    public void Load(String path) {
//		load the yaml testset to a YamlCase object
		YamlCase yCase = YamlUtil.load(path);
	    
	    name = yCase.getConfig().getName();
//		get the variables in config
		Map<String,Object> configVariables = yCase.getConfig().getVariables();
//      处理config中variables的参数替换
		Utils.prepareVariables(configVariables,varsValue,varsFuncFormula);
		
		baseUrl = yCase.getConfig().getRequest().getBaseUrl();
		
		Map<String,Object> configHeaders = yCase.getConfig().getRequest().getHeaders();
		
        //处理headers中variables的参数替换
		Utils.prepareVariables(configHeaders,headerValue,headerFuncFormula);

		//load yCase中的所有testcase，转换成Test对象，存到本类的tests成员变量中
		
		//load yCase中的所有testcase，返回TestCase[]
		TestCase[] testcases = yCase.getCases();
		for(int i = 0; i < testcases.length; i++) {
			TestCase tc = testcases[i];
			Test test = new Test();
			test.load(tc);
			tests.add(test);
		}
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        System.out.println(json);
		
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
        for(String name : headerFuncFormula.keySet()) {
			FuncObj fObj = headerFuncFormula.get(name);
			try {
				headerValue.put(name, Utils.invokeFunc(fObj,varsValue));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
        
        for(Test t : tests) {
        	
        }
        
        
		System.out.println("global variables: ");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        System.out.println(json);
		
	}
	
	
	public static void main(String[] args) {
		TestSet ts = new TestSet();
		ts.Load("/home/claire/work/eclipse-workspace/apitest/src/test/resource/yaml/testset1.yaml");
//		ts.Run();
		
	}
	
}
