package com.example.to_do.Model;

import java.util.stream.Stream;

public class TodoModel extends Task_Id{
    private String task,due;
    private int status;

    public String getTask() {
        return task;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
