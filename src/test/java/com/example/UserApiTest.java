package com.example;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserApiTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }
    @Test
    @Feature("Register User")
    @Story("User Registration")
    @Description("Test case to register a new user successfully")
    public void testRegisterUserSuccess() {
        String requestBody = "{ \"email\": \"examplet@reqres.in\", \"password\": \"abcdef\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .extract().response();

        String token = response.jsonPath().getString("token");
        assertThat(token, notNullValue());  
        assertThat(token, not(isEmptyString()));
    }
    @Test
    @Feature("Register User")
    @Story("User Registration Failure")
    @Description("Test case to register a user with missing password")
    public void testRegisterUserFailure() {
        String requestBody = "{ \"email\": \"examplet@reqres.in\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .extract().response();
        assertThat(response.jsonPath().getString("error"), equalTo("Missing password"));
    }
    @Test
    @Feature("Update User")
    @Story("User Update")
    @Description("Test case to update user successfully")
    public void testUpdateUserSuccess() {
        String requestBody = "{ \"name\": \"Eve\", \"job\": \"Developer\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/api/users/4")
                .then()
                .statusCode(200)
                .extract().response();

        assertThat(response.jsonPath().getString("name"), equalTo("Eve"));
        assertThat(response.jsonPath().getString("job"), equalTo("Developer"));
    }
    @Test
    @Feature("Update User")
    @Story("User Update Failure")
    @Description("Test case to update user with invalid data")
    public void testUpdateUserFailure() {
        String requestBody = "{ \"name\": \"\", \"job\": \"\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/api/users/4")
                .then()
                .statusCode(400)
                .extract().response();

        assertThat(response.jsonPath().getString("error"), equalTo("Invalid input"));
    }
    @Test
    @Feature("Delete User")
    @Story("User Deletion")
    @Description("Test case to delete a user successfully")
    public void testDeleteUserSuccess() {
        Response response = given()
                .when()
                .delete("/api/users/4")
                .then()
                .statusCode(204)
                .extract().response();
    }
    @Test
    @Feature("Delete User")
    @Story("User Deletion Failure")
    @Description("Test case to delete a non-existing user")
    public void testDeleteUserFailure() {
        Response response = given()
                .when()
                .delete("/api/users/3899")
                .then()
                .statusCode(404)
                .extract().response();
        assertThat(response.jsonPath().getString("error"), equalTo("Not Found"));
    }
    @Test
    @Feature("Get User")
    @Story("User Retrieval")
    @Description("Test case to get a user successfully")
    public void testGetUserSuccess() {
        Response response = given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .extract().response();
        assertThat(response.jsonPath().getString("data.first_name"), equalTo("Emma"));
        assertThat(response.jsonPath().getString("data.last_name"), equalTo("Wong"));
    }
    @Test
    @Feature("Get User")
    @Story("User Retrieval Failure")
    @Description("Test case to get a non-existing user")
    public void testGetUserFailure() {
        Response response = given()
                .when()
                .get("/api/users/777777")
                .then()
                .statusCode(404)
                .extract().response();
        assertThat(response.jsonPath().getString("error"), equalTo("Not Found"));
    }
}

