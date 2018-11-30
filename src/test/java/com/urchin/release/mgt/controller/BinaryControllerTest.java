package com.urchin.release.mgt.controller;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BinaryControllerTest {

    private RequestSpecification restAssured;

    @LocalServerPort
    int randomServerPort;

    @Before
    public void setup(){
        restAssured = RestAssured.given().baseUri("http://localhost:" + randomServerPort + "/");
    }

    @Test
    public void redirectToLoginPage(){
        restAssured.redirects().follow(false).get("binaries/info")
                .then().statusCode(302).header("Location", "http://localhost:" + randomServerPort + "/login");
    }

}
