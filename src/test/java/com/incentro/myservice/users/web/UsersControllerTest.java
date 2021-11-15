package com.incentro.myservice.users.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incentro.myservice.config.MyServiceApplicationTests;
import com.incentro.myservice.config.TestCredentialsUtility;
import com.incentro.myservice.users.entity.User;
import com.incentro.myservice.users.dto.AddUserDTO;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UsersControllerTest extends MyServiceApplicationTests {

    private final ObjectMapper mapper = new ObjectMapper();
    private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private AddUserDTO addUserDTO;

    @Before
    public void setUp() {
        addUserDTO = new AddUserDTO();
        addUserDTO.setFirstName("Jane");
        addUserDTO.setLastName("Doe");
        addUserDTO.setPhoneNumber("0711000000");
        addUserDTO.setEmail("jane.doe@dev.com");
        addUserDTO.setActive(Boolean.TRUE);
        addUserDTO.setRole(User.Role.MEMBER);

    }

    @Test
    public void addUserFailsInvalidPayload() throws JsonProcessingException {
        AddUserDTO addUserDTO = new AddUserDTO();

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(400)
                .body(containsString("must not be blank"));

    }

    @Test
    public void addUserFailsInvalidEmail() throws JsonProcessingException {
        addUserDTO.setEmail("akhdhkj.com");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(400)
                .body(containsString("Email must be a well-formed email address"));

    }

    @Test
    public void addUserFailsInvalidPhoneNumber() throws JsonProcessingException {
        addUserDTO.setPhoneNumber("08004975dshfjd");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(400)
                .body(containsString("Phone number contains invalid characters"));

    }

    @Test
    public void addUserFailsInvalidCharacterLengths() throws JsonProcessingException {
        addUserDTO.setFirstName("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore r adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore , sed do eiusmod tempor incididunt ut labore et dolore ");
        addUserDTO.setLastName("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore r adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore , sed do eiusmod tempor incididunt ut labore et dolore ");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(400)
                .body(containsString("First name maximum allowed characters is 200"));

    }

    @Test
    public void addUserFailsEmailExist() throws JsonProcessingException {
        addUserDTO.setEmail("user1@dev.com");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(400)
                .body(containsString("Email exist"));

    }

    @Test
    public void addUserFailsPhoneNumberExist() throws JsonProcessingException {
        addUserDTO.setPhoneNumber("0700000002");
        addUserDTO.setEmail("jane.doe2@dev.com");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(400)
                .body(containsString("Phone number exist"));

    }

    @Test
    public void addUserWorks() throws JsonProcessingException {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addUserDTO))
                .post("/users")
                .then()
                .statusCode(200)
                .body("data.email", equalTo(addUserDTO.getEmail()))
                .body("data.phoneNumber", equalTo(addUserDTO.getPhoneNumber()))
                .body("data.firstName", equalTo(addUserDTO.getFirstName()))
                .body("data.lastName", equalTo(addUserDTO.getLastName()))
                .body("data.active", equalTo(addUserDTO.isActive()))
                .body("data.role", equalTo(addUserDTO.getRole().getAuthority()));

    }

    @Test
    public void getUsersWorks() {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .get("/users")
                .then()
                .statusCode(200)
                .body("data.size()", greaterThanOrEqualTo(2));

    }

    @Test
    public void getUsersFailsInvalidAccess() {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .get("/users")
                .then()
                .statusCode(403);

    }

    @Test
    public void getUserFailsInvalidUserId() {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .get("/users/{userId}", 234342)
                .then()
                .statusCode(400)
                .body(containsString("User account does not exist"));

    }

    @Test
    public void getUserWorks() {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .get("/users/{userId}", 1)
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1))
                .body("data.email", equalTo("user1@dev.com"))
                .body("data.phoneNumber", equalTo("0700000001"))
                .body("data.firstName", equalTo("user_first_1"))
                .body("data.lastName", equalTo("user_last_1"))
                .body("data.active", equalTo(true))
                .body("data.role", equalTo("Librarian"));

    }
}
