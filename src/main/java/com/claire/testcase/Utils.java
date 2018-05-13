package com.claire.testcase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	private static final String regexFunc = "\\$\\{([\\w]+)\\(([\\w,\\s\\$\\.]*)\\)\\}";
	public static Object findVarValue(String varName) {
		//第一步 先取全局里的
		//第二步 取本case里的，同名的覆盖
		return null;
	}
	
	public static Object invokeFunc(FuncObj fn) {
		//call findVarValue if there's any params need to be replaced by variable
		return null;
		
	}
	
	public static void prepareVariablesByFunc(Map<String,FuncObj> funcFormula,Map<String,Object> varsByFunc) {
		for(String varName : funcFormula.keySet()){
			FuncObj varFn = funcFormula.get(varName);
			Object varValue = Utils.invokeFunc(varFn);
			if(varValue==null){
				//TODO: throw exception
			}
			varsByFunc.put(varName, varValue);
		}
	}

	/* variables可能存在两种形式，1.固定值；2.需要函数计算
	 * 固定值的variable，赋值给varsFixed
	 * 需要函数计算的variable，赋值给varsByFunc*/
	public static void prepareVariables(Map<String,Object> variables,Map<String, Object> varsFixed,Map<String, Object> varsByFunc) {
		
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
					
					//call the function to calculate the variables
					//TODO 
					String val;
					try {
						val = funcInvoke("com.claire.testcase.Funcs", funcName, paramList);
						varsByFunc.put(name, val);
						System.out.printf("[varsByFunc]name:%s; value:%s\n", name, val);
						continue;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
			
			varsFixed.put(name, obj);
			System.out.println("[varsFixed]name: " + name + ";" + " value: " + obj.toString());
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
				System.out.println(m);
				System.out.println(params);
				
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

		return retVal;
	}	
}
