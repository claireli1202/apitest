package com.claire.apitest;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.claire.tools.IRequest;
import com.claire.tools.IValidate;
import com.claire.tools.YamlCase;
import com.claire.tools.YamlUtil;

import org.testng.annotations.BeforeTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.greaterThan;
import io.restassured.RestAssured;
import io.restassured.config.DecoderConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetTest {
	YamlCase yCase = null;
	Map<String,Object> extractDataValue = new HashMap<String , Object>();
	

	@BeforeTest
	public void setUp() {
		yCase = YamlUtil.load("C:\\Users\\claire\\workspace\\api-test\\src\\test\\resources\\yaml\\tc_get.yaml");
		RestAssured.baseURI = yCase.getConfig().getBaseURI();

	}

	@Test
	public void testGet() {
		
		System.out.println("testGet - number of cases:" + yCase.getCases().length);
		for (int i = 0; i < yCase.getCases().length; i++) {
			System.out.println("Test for case: " + i);
			IRequest req = yCase.getCases()[i].getTestCase().getRequest();
			Map<String, Object> params = req.getParams();
			
			RequestSpecification rs = given().
					config(RestAssured.config().
					decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8")));			
//			Check if has any $variable in params and replace it
			if (params != null){
				if(extractDataValue != null){
					YamlUtil.replaceVariable(params, extractDataValue);					
				}
				rs = rs.params(params);
			}			
			
			ExtractableResponse<Response> er = rs.
//					log().all().
//					expect()
//						.parser(ContentType.HTML.toString(), Parser.JSON).
					when().
					    get(req.getPath()).
					then().
//						statusCode(200).
//						body("status", equalTo("200")).
					// contentType(ContentType.HTML).
					extract();
			
			
//			Get the path of the extracted data in JSON response
//			And save into Map<String,Object> extractDataValue
//			Like {"$a":"350757819118","$b":1}
//			Map<String,String> extractDataPath = yCase.getCases()[i].getTestCase().getExtract();
//			if (extractDataPath != null){
//				for (String key : extractDataPath.keySet()){
//					extractDataValue.put(key, er.path(extractDataPath.get(key)));
//					 
//				}
////				Print the Map of extractDataValue
////				if (extractDataValue!=null){
////					System.out.println("Map<String,Object> extractDataValue:");
////					for (String key : extractDataValue.keySet()){
////						System.out.println(key + ":" + extractDataValue.get(key));
////					}
////				}
//			}
//			
			
			Response response = er.response();
			 String body = response.getBody().asString();
			 String headerValue = response.getHeader("Host");
			 String cookieValue = response.getCookie("WWWID");
			
//			print response
//			response.getBody().prettyPrint();
			 System.out.println(body);
			 System.out.println(headerValue);
			 System.out.println(cookieValue);
			
//			validate response
//			IValidate val = yCase.getCases()[i].getTestCase().getValidate();
//			if (val != null) {
//				response.
//				then().
//					statusCode(val.getStatusCode()).
//					contentType(ContentType.HTML);
//
//				Map<String, Object> expResBody = val.getResponseBody();
//				if (expResBody != null) {
//					for (String key : expResBody.keySet()) {
//						try{
//							response.then().body(key, equalTo(expResBody.get(key)));
//							System.out.println("==================");
//						}catch(Exception e) {
//							System.out.println(e);
//						}
//						
//					}
//				}
//			}

		}

	}

}
