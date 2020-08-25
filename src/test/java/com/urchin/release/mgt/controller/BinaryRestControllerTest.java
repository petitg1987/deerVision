package com.urchin.release.mgt.controller;

import com.urchin.release.mgt.config.properties.BinaryProperties;
import com.urchin.release.mgt.service.BinaryService;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "awssecurity"})
public class BinaryRestControllerTest {

    private RequestSpecification restAssured;
    private RequestSpecification restAssuredAuth;

    @Autowired
    private BinaryProperties binaryProperties;

    @Autowired
    private BinaryService binaryService;

    @LocalServerPort
    int randomServerPort;

    @Before
    public void setup(){
        restAssured = RestAssured.given().baseUri("http://localhost:" + randomServerPort + "/");
        restAssuredAuth = RestAssured.given().baseUri("http://localhost:" + randomServerPort + "/").auth().preemptive().basic("api", "password");
    }

    @Test
    public void checkSecure(){
        restAssured.put("api/binaries/linux-deb").then().statusCode(401);
    }

    //@Test
    public void uploadWithoutVersion(){
        String filename = "test.deb";
        restAssuredAuth.multiPart("file", filename, "fileContent".getBytes()).put("api/binaries/linux-deb")
                .then().statusCode(400);
    }

    //@Test
    public void uploadAndDeleteTest(){
        //upload
        String filename = "test-0.0.0.deb";
        String fileContent = "fileContent";
        restAssuredAuth.multiPart("file", filename, fileContent.getBytes()).put("api/binaries/linux-deb")
                .then().statusCode(200);

        //delete
        binaryService.deleteIfExist(filename);
    }

}