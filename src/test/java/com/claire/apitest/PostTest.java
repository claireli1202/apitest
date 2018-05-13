package com.claire.apitest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.config.DecoderConfig;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.claire.tools.IRequest;
import com.claire.tools.IValidate;
import com.claire.tools.YamlCase;
import com.claire.tools.YamlUtil;

public class PostTest {
	YamlCase yCase = null;

	@BeforeTest
	public void setUp() {
		yCase = YamlUtil
				.load("C:\\Users\\claire\\workspace\\api-test\\src\\test\\resources\\yaml\\tc_post.yaml");
		RestAssured.baseURI = yCase.getConfig().getBaseURI();
	}

	@Test
	public void testPost() {
		System.out.println("testPost - number of cases:" + yCase.getCases().length);
		for (int i = 0; i < yCase.getCases().length; i++) {
			IRequest req = yCase.getCases()[i].getTestCase().getRequest();
//			Map<String, Object> formParams = req.getFormParams();
			RequestSpecification rs = given().
					config(RestAssured.config().
					decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8")));
//			if (formParams != null) {
//				rs = rs.formParams(formParams);
//			}

			Response response = 
					rs.
					log().all().
					expect().
						defaultParser(Parser.JSON).
					when().
						post(req.getPath()).
					then().
					extract().
					    response();
			
			// print response
			response.getBody().prettyPrint();
		
//			validate response
			IValidate val = yCase.getCases()[i].getTestCase().getValidate();
			if (val != null) {
				response.
				then().
					statusCode(val.getStatusCode());

				Map<String, Object> expResBody = val.getResponseBody();
				if (expResBody != null) {
					for (String key : expResBody.keySet()) {
//						System.out.println(key + " : " + expResBody.get(key));
//						System.out.println("actual value:" + response.getBody().jsonPath().get(key));
						response.then().body(key, equalTo(expResBody.get(key)));
					}
				}
			}

		}

	}
}
