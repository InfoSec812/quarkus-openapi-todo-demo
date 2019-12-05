package com.redhat.quarkus.demo.todo;

import com.redhat.quarkus.demo.todo.client.ApiClient;
import com.redhat.quarkus.demo.todo.client.ApiException;
import com.redhat.quarkus.demo.todo.client.api.DefaultApi;
import com.redhat.quarkus.demo.todo.client.models.Todo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class RestIntegrationTest {

  @Test
  public void testCreateTodo() {
    ApiClient client = new ApiClient();
    client.setBasePath("http://localhost:8081");
    DefaultApi apiClient = new DefaultApi(client);

    Todo newTodo = new Todo();
    newTodo.setTitle("Test title");
    newTodo.setDescription("Test description");
    newTodo.setDueDate(OffsetDateTime.now());
    newTodo.setComplete(false);
    try {
      apiClient.createTodo(newTodo);
      Todo persistedTodo = apiClient.gettodos().get(0);
      assertEquals(persistedTodo.getTitle(), newTodo.getTitle());
      assertEquals(persistedTodo.getDueDate(), newTodo.getDueDate());
      assertEquals(persistedTodo.getComplete(), newTodo.getComplete());
      assertEquals(persistedTodo.getDescription(), newTodo.getDescription());
    } catch (ApiException e) {
      fail(e);
    }
  }
}
