package com.example.smarttodolist;

public class Task {
    private int id;
    private String title;
    private String priority;
    private int isDone;
    private String date;

    public Task() {}

    public Task(int id, String title, String priority, int isDone, String date) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.isDone = isDone;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public int getIsDone() { return isDone; }
    public void setIsDone(int isDone) { this.isDone = isDone; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}