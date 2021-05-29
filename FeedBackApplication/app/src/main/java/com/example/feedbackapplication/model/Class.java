package com.example.feedbackapplication.model;

public class Class {
    private int classID;
    private String className;
    private int capacity;

    public Class() {

    }

    public Class(int classID, String className, int capacity) {
        this.classID = classID;
        this.className = className;
        this.capacity = capacity;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
