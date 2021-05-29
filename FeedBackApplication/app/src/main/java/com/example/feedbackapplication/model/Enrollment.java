package com.example.feedbackapplication.model;

public class Enrollment {
    private int classId;
    private String trainee;


    public Enrollment() {
    }

    public Enrollment(int classId, String trainee) {
        this.classId = classId;
        this.trainee = trainee;
    }

    public String getTrainee() {
        return trainee;
    }

    public void setTrainee(String trainee) {
        this.trainee = trainee;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}