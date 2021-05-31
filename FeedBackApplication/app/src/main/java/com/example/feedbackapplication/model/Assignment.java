package com.example.feedbackapplication.model;

public class Assignment {
    private int ModuleID;
    private int ClassID;
    private String TrainerID;
    private String Code;

    public Assignment(){

    }

    public Assignment(int moduleID, int classID, String trainerID, String code) {
        ModuleID = moduleID;
        ClassID = classID;
        TrainerID = trainerID;
        Code = code;
    }

    public int getModuleID() {
        return ModuleID;
    }

    public void setModuleID(int moduleID) {
        ModuleID = moduleID;
    }

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int classID) {
        ClassID = classID;
    }

    public String getTrainerID() {
        return TrainerID;
    }

    public void setTrainerID(String trainerID) {
        TrainerID = trainerID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
