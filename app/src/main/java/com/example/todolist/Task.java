package com.example.todolist;

import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private String time;
    private boolean isCompleted;

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    private boolean isImportant; // Add this field


    public Task(String title, String description, String time) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.isCompleted = false;
        this.isImportant = false; // Default to false
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTime() { return time; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                Objects.equals(time, task.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, time);
    }
}
