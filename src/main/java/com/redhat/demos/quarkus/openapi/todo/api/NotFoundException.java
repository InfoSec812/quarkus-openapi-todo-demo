package com.redhat.demos.quarkus.openapi.todo.api;

public class NotFoundException extends Exception {

  public NotFoundException(Throwable t) {
    this.initCause(t);
  }
}
