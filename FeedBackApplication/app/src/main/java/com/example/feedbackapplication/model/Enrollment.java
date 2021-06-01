package com.example.feedbackapplication.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Enrollment {
    private int ClassID;
    private String TraineeID;

    public Enrollment() {

    }

    public Enrollment(int classID, String traineeID) {
        this.ClassID = classID;
        this.TraineeID = traineeID;
    }


    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int classID) {
        this.ClassID = classID;
    }

    public String getTraineeID() {
        return TraineeID;
    }

    public void setTraineeID(String traineeID) {
        this.TraineeID = traineeID;
    }
}