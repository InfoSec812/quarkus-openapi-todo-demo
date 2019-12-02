package com.redhat.demos.quarkus.openapi.todo.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TodosApiServiceImplTest {

    String firstTodo = "{\"title\": \"Some ToDo\", \"description\": \"My first todo\", \"complete\": false, \"dueDate\": null}";
    String secondTodo = "{\"title\": \"Another ToDo\", \"description\": \"My second todo\", \"complete\": false, \"dueDate\": null}";

    @Test
    public void testAddingTodos() {
      given()
          .body(firstTodo)
          .contentType(JSON)
          .when().post("/todos")
          .then()
            .contentType(JSON)
            .body("title", equalTo("Some ToDo"));
    }

    @Test
    public void testCountingTodos() {
        Response response = given()
            .when()
                .get("/todos")
            .then()
                .extract()
                .response();
        assertTrue(response.jsonPath().getList("id").size() >= 3);
    }

    @Test
    public void testDeleteTodo() {
        Response beforeDelete = given()
            .when()
            .get("/todos")
            .then()
            .extract()
            .response();
        given()
            .when()
                .delete("/todos/1")
            .then()
                .statusCode(NO_CONTENT.getStatusCode());
        Response afterDelete = given()
            .when()
            .get("/todos")
            .then()
            .extract()
            .response();
        assertTrue((beforeDelete.jsonPath().getList("id").size() - afterDelete.jsonPath().getList("id").size()) == 1);
    }

    @Test
    public void testUpdateTodo() {
        given()
            .body(secondTodo)
            .contentType(JSON)
        .when()
            .put("/todos/2")
        .then()
            .contentType(JSON)
            .body("id", equalTo(2))
            .body("title", equalTo("Another ToDo"));

        given()
            .when().get("/todos/2")
            .then()
            .contentType(JSON)
            .body("title", equalTo("Another ToDo"));
    }
}