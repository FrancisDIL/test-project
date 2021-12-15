package com.incentro.myservice.books.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incentro.myservice.books.dto.AddBookDTO;
import com.incentro.myservice.books.dto.EditBookDTO;
import com.incentro.myservice.books.entity.Book;
import com.incentro.myservice.config.MyServiceApplicationTests;
import com.incentro.myservice.config.TestCredentialsUtility;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BooksControllerTest extends MyServiceApplicationTests {

    private final ObjectMapper mapper = new ObjectMapper();
    private AddBookDTO addBookDTO;

    @Before
    public void setUp() throws Exception {
        addBookDTO = new AddBookDTO();
        addBookDTO.setAuthor("Jared Diamond");
        addBookDTO.setTitle("Guns Germs and Steel");
    }

    @Test
    public void addBookWorks() throws JsonProcessingException {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addBookDTO))
                .post("/books")
                .then()
                .statusCode(200)
                .body("data.id",notNullValue())
                .body("data.author", equalTo(addBookDTO.getAuthor()))
                .body("data.title", equalTo(addBookDTO.getTitle()));
    }

    @Test
    public void addBookFailsNoPermissions() throws JsonProcessingException {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addBookDTO))
                .post("/books")
                .then()
                .statusCode(403);
    }

    @Test
    public void addBookFailsNoAuthor() throws JsonProcessingException {
        AddBookDTO noAuthorBookDTO   = new AddBookDTO();
        noAuthorBookDTO.setTitle("Guns, Germs and Steel");
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(noAuthorBookDTO))
                .post("/books")
                .then()
                .statusCode(400)
                .body(containsString("Book author cannot be empty"));
    }

    @Test
    public void addBookFailsNoTitle() throws JsonProcessingException {
        AddBookDTO noTitleBookDTO = new AddBookDTO();
        noTitleBookDTO.setAuthor("Jared Diamond");
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(noTitleBookDTO))
                .post("/books")
                .then()
                .statusCode(400)
                .body(containsString("Book title cannot be empty"));
    }

    @Test
    public void addBookFailsAuthorTooLong() throws JsonProcessingException {
        AddBookDTO longAuthorBookDTO = new AddBookDTO();
        longAuthorBookDTO.setTitle("Guns Germs and Steel");
        longAuthorBookDTO.setAuthor("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore r adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore , sed do eiusmod tempor incididunt ut labore et dolore ");
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(longAuthorBookDTO))
                .post("/books")
                .then()
                .statusCode(400)
                .body(containsString("Book author cannot exceed 200 characters"));
    }

    @Test
    public void addBookFailsTitleTooLong() throws JsonProcessingException {
        AddBookDTO longTitleBookDTO = new AddBookDTO();
        longTitleBookDTO.setAuthor("Jared Diamond");
        longTitleBookDTO.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore r adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore , sed do eiusmod tempor incididunt ut labore et dolore ");
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(longTitleBookDTO))
                .post("/books")
                .then()
                .statusCode(400)
                .body(containsString("Book title cannot exceed 200 characters"));
    }

    @Test
    public void getBookWorks() {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .get("/books/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.author", equalTo("Paul Johnson"))
                .body("data.title", equalTo("Intellectuals"))
                .body("data.status", equalTo("AVAILABLE"));
    }

    @Test
    public void getBookFailsNoExist() throws JsonProcessingException {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(addBookDTO))
                .get("/books/1300")
                .then()
                .statusCode(400)
                .body(containsString("Book does not exist"));
    }

    @Test
    public void getAllBooksWorks() {
        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .get("/books")
                .then()
                .statusCode(200)
                .body(containsString("Modern English Dictionary"));
    }

    @Test
    public void editBookWorks() throws JsonProcessingException {
        EditBookDTO editBookDTO = new EditBookDTO();
        editBookDTO.setId(1L);
        editBookDTO.setTitle("Pale Blue Dot - A Vision of the Human Future in Space");
        editBookDTO.setStatus(Book.BookStatus.BORROWED);

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(editBookDTO))
                .put("/books")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1))
                .body("data.author", equalTo("Carl Sagan"))
                .body("data.title", equalTo("Pale Blue Dot - A Vision of the Human Future in Space"))
                .body("data.status", equalTo("BORROWED"));
    }

    @Test
    public void editBookFailsNoExist() throws JsonProcessingException {
        EditBookDTO editBookDTO = new EditBookDTO();
        editBookDTO.setId(1200L);
        editBookDTO.setAuthor("Robert Green");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(editBookDTO))
                .put("/books")
                .then()
                .statusCode(400);
    }

    @Test
    public void editBookFailsNoPermissions() throws JsonProcessingException {
        EditBookDTO editBookDTO = new EditBookDTO();
        editBookDTO.setId(3L);
        editBookDTO.setAuthor("Leo Tolstoy");

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .body(mapper.writeValueAsString(editBookDTO))
                .put("/books")
                .then()
                .statusCode(403);
    }

    @Test
    public void borrowBookWorks() {

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .put("/books/borrow/4")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(4))
                .body("data.author", equalTo("Fyodor Dostoyevsky"))
                .body("data.title", equalTo("The Brothers Karamazov"))
                .body("data.status", equalTo("BORROWED"));
    }

    @Test
    public void borrowBookFailsNotAvailable() {

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .put("/books/borrow/5")
                .then()
                .statusCode(400)
                .body(containsString("The requested book is not available"));
    }

    @Test
    public void returnBookWorks() {

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .put("/books/return/9")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(9))
                .body("data.author", equalTo("J.K. Sprawlings"))
                .body("data.title", equalTo("The Theory of Returnables"))
                .body("data.status", equalTo("AVAILABLE"));
    }

    @Test
    public void deleteBookWorks() {

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .delete("/books/6")
                .then()
                .statusCode(200)
                .body(containsString("Book deleted successfully"));
    }

    @Test
    public void deleteBookFailsNoExist() {

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getAdministrator_user_1_token())
                .contentType(ContentType.JSON)
                .delete("/books/1200")
                .then()
                .statusCode(400)
                .body(containsString("Book does not exist"));
    }

    @Test
    public void deleteBookFailsNoPermissions() {

        given()
                .auth().oauth2(TestCredentialsUtility.getInstance().getMember_user_2_token())
                .contentType(ContentType.JSON)
                .delete("/books/7")
                .then()
                .statusCode(403);
    }
}