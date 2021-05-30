package com.example.feedbackapplication.model;

public class Class {
    private int classID;
    private String className;
    private int capacity;
    private String startDate;
    private String endDate;

    public Class() {

    }

    public Class(int classID, String className, int capacity, String startDate, String endDate) {
        this.classID = classID;
        this.className = className;
        this.capacity = capacity;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
