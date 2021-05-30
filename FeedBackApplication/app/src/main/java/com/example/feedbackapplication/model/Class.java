package com.example.feedbackapplication.model;

public class Class {
    private int ClassID;
    private String ClassName;
    private int Capacity;
    private String StartDate;
    private String EndDate;

    public Class() {

    }

    public Class(int classID, String className, int capacity, String startDate, String endDate) {
        ClassID = classID;
        ClassName = className;
        Capacity = capacity;
        StartDate = startDate;
        EndDate = endDate;
    }

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int classID) {
        ClassID = classID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
