package com.incentro.myservice.config;

import com.incentro.myservice.application.config.constant.Oauth2Configurations;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class MyServiceApplicationTests {

    @LocalServerPort
    public int port;

    @Before
    public void setUpGlobal() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        String password = "password";
        if (TestCredentialsUtility.getInstance().administrator_user_1_token.length() == 0) {
            TestCredentialsUtility.getInstance().administrator_user_1_token.append(obtainJwtTokenSuccess(TestCredentialsUtility.getInstance().ADMINISTRATOR_USER_1_USERNAME, password));
        }
        if (TestCredentialsUtility.getInstance().member_user_2_token.length() == 0) {
            TestCredentialsUtility.getInstance().member_user_2_token.append(obtainJwtTokenSuccess(TestCredentialsUtility.getInstance().MEMEBER_USER_2_USERNAME, password));
        }
    }

    public String obtainJwtTokenSuccess(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("username", username);
        map.put("password", password);

        String jwt = given()
                .auth().basic(Oauth2Configurations.CLIENT_ID, Oauth2Configurations.CLIENT_SECRET)
                .formParams(map)
                .post("oauth/token")
                .then()
                .statusCode(200)
                .extract().path("access_token");
        return jwt;
    }

    public Response obtainJwtTokenFails(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("username", username);
        map.put("password", password);

        return given()
                .auth().basic(Oauth2Configurations.CLIENT_ID, Oauth2Configurations.CLIENT_SECRET)
                .formParams(map)
                .post("oauth/token");
    }

    public String getJWTBody(String jwtTokenString) {
        System.out.println("------------ Decode JWT ------------");
        String[] split_string = jwtTokenString.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        //System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
        Base64 base64Url = new Base64(true);
        //String header = new String(base64Url.decode(base64EncodedHeader));
        //System.out.println("JWT Header : " + header);


        //System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        //System.out.println("JWT Body : " + body);
        return body;
    }
}
