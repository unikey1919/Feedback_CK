package com.example.feedbackapplication.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Answer {
    private int ClassID;
    private int ModuleID;
    private int QuestionID;
    private String Trainee;
    private int Value;

    public Answer() {

    }

    public Answer(int ClassID, int ModuleID, int QuestionID,String Trainee,int Value) {
        this.ClassID = ClassID;
        this.ModuleID = ModuleID;
        this.QuestionID = QuestionID;
        this.Trainee = Trainee;
        this.Value = Value;
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

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public String getTrainee() {
        return Trainee;
    }

    public void setTrainee(String trainee) {
        Trainee = trainee;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }
}