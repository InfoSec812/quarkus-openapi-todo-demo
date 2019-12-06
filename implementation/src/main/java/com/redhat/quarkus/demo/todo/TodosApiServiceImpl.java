package com.redhat.quarkus.demo.todo;

import com.redhat.demos.quarkus.openapi.todo.api.NotFoundException;
import com.redhat.demos.quarkus.openapi.todo.api.TodosApiService;
import com.redhat.demos.quarkus.openapi.todo.models.Todo;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
public class TodosApiServiceImpl implements TodosApiService {
  @Override
  @Transactional
  public Response createTodo(Todo todo, SecurityContext securityContext) throws NotFoundException {
    Todo.persist(todo);
    todo.flush();
    return Response.ok(todo).build();
  }

  @Override
  @Transactional
  public Response deleteTodo(String todoId, SecurityContext securityContext) throws NotFoundException {
    Todo.findById(todoId).delete();
    return Response.noContent().build();
  }

  @Override
  public Response getTodo(String todoId, SecurityContext securityContext) throws NotFoundException {
    return Response.ok(Todo.findById(todoId)).build();
  }

  @Override
  public Response gettodos(SecurityContext securityContext) throws NotFoundException {
    return Response.ok(Todo.listAll()).build();
  }

  @Override
  @Transactional
  public Response updateTodo(String todoId, Todo todo, SecurityContext securityContext) throws NotFoundException {
    Todo existing = Todo.findById(todoId);
    existing.setTitle(todo.getTitle());
    existing.setDescription(todo.getDescription());
    existing.setComplete(todo.isComplete());
    existing.setDueDate(todo.getDueDate());
    existing.persistAndFlush();
    return Response.ok(existing).build();
  }
}
