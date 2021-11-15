package com.incentro.myservice.application.web;

import com.incentro.myservice.config.MyServiceApplicationTests;
import com.incentro.myservice.config.TestCredentialsUtility;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class IndexControllerTest extends MyServiceApplicationTests {

    @Test
    public void indexTestAdministratorAccess(){

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .get("/index/test/admin")
                .then()
                .statusCode(200)
                .body(containsString("Test Endpoint"));
    }

    @Test
    public void indexTestMemberAccess(){

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .get("/index/test/member")
                .then()
                .statusCode(200)
                .body(containsString("Test Endpoint"));
    }
}
