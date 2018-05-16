package com.claire.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
	
	private static final String regexFunc = "\\$\\{([\\w]+)\\(([\\w,\\s\\$\\.]*)\\)\\}";
	private static final String regexVariable = "^\\$([\\w]+)";
	public static Object findVarValue(String varName, Map<String, Object> varValues) {
//		查找是否有匹配的varName
		for (String name : varValues.keySet()) {
			if(varName.equals(name)) {
				return varValues.get(name);
			}
		}
		return null;
	}
	
	
	public static Object invokeFunc(FuncObj fn, Map<String,Object> varValues) throws Exception {
		System.out.println("begin call invokeFunc");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(varValues);
        System.out.println(json);
        
		//call findVarValue if there's any params need to be replaced by variable
		List<String> paramsRuntime = new ArrayList<String>();
		
		List<String> params = fn.getFuncParams();
		for(String p : params) {
			Object varValue = p;
			Pattern pattern = Pattern.compile(regexVariable);
			Matcher matcher = pattern.matcher(p);
			if(matcher.find()){
				String varName = matcher.group(1);
				varValue = findVarValue(varName,varValues);
				if (varValue == null) {
//					TODO:throw exception
					String ex = String.format("函数%s: 参数(%s)未找到定义", fn.getFuncName(), varName);
					throw new Exception(ex);
				}
			}
			paramsRuntime.add(varValue.toString());
		}
		
		//call the function to calculate the variables
		//TODO 
		String val = "";
		try {
			val = funcInvoke("com.claire.testcase.Funcs", fn.getFuncName(), paramsRuntime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		return val;
		
	}
	

	/* variables可能存在两种形式，1.固定值；2.需要函数计算
	 * 固定值的variable，赋值给varsValue
	 * 需要函数计算的variable，赋值给varsFuncFormula
	 * 运行是计算好之后再赋值给varsValue*/
	public static void prepareVariables(Map<String,Object> variables, Map<String, Object> varsValue, Map<String, FuncObj> varsFuncFormula) {
		
		for(String name : variables.keySet()) {
			Object obj = variables.get(name);
			if(obj instanceof String) {
				String str = (String)obj;
				Pattern pattern = Pattern.compile(regexFunc);
				Matcher matcher = pattern.matcher(str);
				
				if(matcher.find()){
					String funcName = matcher.group(1);
					String[] params = matcher.group(2).split(",");
					
					List<String> paramList = new ArrayList<String>();
					System.out.printf("[varsByFunc]name: %s; funcName: %s; funcParamList: \n", name, funcName);
					for (int i=0; i< params.length; i++){
						System.out.printf("%s\t", params[i].trim());
						paramList.add(params[i].trim());
					}
					System.out.println();
					
//					generate the funcObj
					FuncObj fObj = new FuncObj(funcName, paramList);
					varsFuncFormula.put(name, fObj);
					continue;
				}
			}
			
			varsValue.put(name, obj);
			System.out.println("[varsValue]name: " + name + ";" + " value: " + obj.toString());
		}
		
	}

	public static String funcInvoke(String className, String method, List<String> args) throws Exception {
		String retVal = "";	
		boolean found = false; 
		try {
			Class<?> c = Class.forName(className);
			 Method[] allMethods = c.getDeclaredMethods();
			 for (Method m : allMethods) {
				if (!m.getName().equals(method)) {
				    continue;
				}
				found = true;
//				System.out.println("find method ok!");
				//统一返回string类型
				if (!m.getReturnType().equals(String.class)) {
					//todo
					throw new Exception("调用的函数返回类型不是String");
				}
				Class<?>[] pType  = m.getParameterTypes();
				if (pType.length != args.size()) {
					throw new Exception("传入的参数个数与函数原型参数个数不匹配");
				}
				List<Object> params = new ArrayList<Object>();
				for (int i = 0; i < pType.length; i++) {
					Class<?> p = pType[i];
					String argStr = args.get(i);
					if (p.equals(String.class)) {
						params.add(argStr);
					} else if (p.equals(int.class)) {
						params.add(Integer.parseInt(argStr));
					} else if (p.equals(long.class)) {
						params.add(Long.parseLong(argStr, 10));
					} else if (p.equals(double.class)) {
						params.add(Double.parseDouble(argStr));
					} else if (p.equals(boolean.class)) {
						params.add(Boolean.parseBoolean(argStr));
					} else {
						String ex = String.format("传入的参数: 第%d个, 类型<%s>不支持", i+1, p);
						throw new Exception(ex);
					}
				}
				
				//开始调用方法
				Object obj = c.newInstance();
				Object ret = m.invoke(obj, params.toArray());
				retVal = (String)ret;
				break;
			 }
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		} catch (NumberFormatException x) {
			x.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		if(!found) {
			throw new Exception("方法未找到！");
		}
		return retVal;
	}	
}
