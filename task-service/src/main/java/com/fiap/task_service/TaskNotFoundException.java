package com.fiap.task_service;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(final Long id){
        super("Task with id " + id + " not found");
    }
}
