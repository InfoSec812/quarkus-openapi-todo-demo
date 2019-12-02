package com.redhat.demos.quarkus.openapi.todo.api;

import com.redhat.demos.quarkus.openapi.todo.models.Todo;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.*;

@ApplicationScoped
public class TodosApiServiceImpl implements TodosApiService {

  @Override
  @Transactional
  public Response createTodo(Todo todo, SecurityContext securityContext) throws NotFoundException {
    Todo.persist(todo);
    return Response.ok(todo, APPLICATION_JSON).build();
  }

  @Override
  @Transactional
  public Response deleteTodo(Long todoId, SecurityContext securityContext) throws NotFoundException {
    Todo.findById(todoId).delete();
    return Response.noContent().build();
  }

  @Override
  public Response getTodo(Long todoId, SecurityContext securityContext) throws NotFoundException {
    return Response.ok(Todo.findById(todoId), APPLICATION_JSON).build();
  }

  @Override
  public Response gettodos(SecurityContext securityContext) throws NotFoundException {
    return Response.ok(Todo.listAll(), APPLICATION_JSON).build();
  }

  @Override
  @Transactional
  public Response updateTodo(Long todoId, Todo todo, SecurityContext securityContext) throws NotFoundException {
    Todo existing = Todo.findById(todoId);
    existing.setComplete(todo.isComplete());
    existing.setDescription(todo.getDescription());
    existing.setDueDate(todo.getDueDate());
    existing.setTitle(todo.getTitle());
    return Response.ok(existing, APPLICATION_JSON).build();
  }
}
