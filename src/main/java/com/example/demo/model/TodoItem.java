package com.example.demo.model;
public class TodoItem {
    private int taskId; // Assuming taskId is auto-incremented in the database
    private String task;
    private boolean completed;

    // Constructor
    public TodoItem(String task) {
        this.task = task;
        this.completed = false;
    }

    // Getter and setter methods (you may need to generate them)

    public int getTaskId() {
        return taskId;
    }

    public String getName() {
        return task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}