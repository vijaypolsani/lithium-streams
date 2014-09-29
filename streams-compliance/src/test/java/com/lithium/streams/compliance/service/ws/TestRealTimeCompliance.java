package com.lithium.streams.compliance.service.ws;

import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;

public class TestRealTimeCompliance {

	@Test
	public void requestSpecificationAllowsSpecifyingHeaders() throws Exception {
		given().headers("client-id", "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=").and().expect().body(
				containsString("Loading Event Data, Checking Server...")).when().get("/compliance/v1/live");
	}

	@Test
	public void testRealTimeUser() {
		given().headers("client-id", "sCe9KITKh8+h1w4e9EDnVwzXBM8NjiilrWS6dOdMNr0=").and().expect().statusCode(200)
				.body("version", equalTo("1.0"), "title", equalTo("EntityUpdated")).when().get("/compliance/v1/live");
	}
}
