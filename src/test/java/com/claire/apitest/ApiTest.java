package com.claire.apitest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
//import io.restassured.parsing.Parser;

import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.expect;

/**
 * Unit test for simple Api.
 */
public class ApiTest 
{
//	@Before
//    public void setUp() {
//        RestAssured.baseURI= "http://www.kuaidi100.com";
//        RestAssured.port = 80;
////        RestAssured.basePath = "/query";
//    }
//
//    @Test
//    public void testApiDemo() {
//    	Response response = 
//    	given().
//    		param("type", "huitongkuaidi").
//    		param("postid", "350757819118").
//    	expect().
//    		parser(ContentType.HTML.toString(), Parser.JSON).
//    	when().	
//    		get("/query").
//    	then().
//    		statusCode(200).contentType(ContentType.HTML).
//    		
//    		body("nu", equalTo("350757819118"),"data.size()", greaterThan(10), "data[0].context",equalTo("广州市|广州市【广州新永和站】，周祥代 已签收")).
//    	extract().
//        	response();
//    	
//    	System.out.println(response.getBody().prettyPrint());
//    }

}
