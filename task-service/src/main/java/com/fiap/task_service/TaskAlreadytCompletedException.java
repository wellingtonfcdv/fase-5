package com.fiap.task_service;

public class TaskAlreadytCompletedException extends RuntimeException {

    public TaskAlreadytCompletedException(final Long id) {
        super("Task with id " + id + " is already completed");
    }
}
