package com.example.feedbackapplication.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Trainee_Comment {
    private int ClassID;
    private int ModuleID;
    private String TraineeID;
    private String Comment;

    public Trainee_Comment() {

    }

    public Trainee_Comment(int classID,int moduleID, String traineeID,String comment) {
        this.ClassID = classID;
        this.ModuleID = moduleID;
        this.TraineeID = traineeID;
        this.Comment = comment;
    }


    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int classID) {
        ClassID = classID;
    }

    public int getModuleID() {
        return ModuleID;
    }

    public void setModuleID(int moduleID) {
        ModuleID = moduleID;
    }

    public String getTraineeID() {
        return TraineeID;
    }

    public void setTraineeID(String traineeID) {
        TraineeID = traineeID;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}