package com.example.feedbackapplication.model;

public class Assignment {
    private int moduleID;
    private int classID;
    private String trainerID;
    private String code;

    public Assignment(){

    }

    public Assignment(int moduleID, int classID, String trainerID, String code) {
        this.moduleID = moduleID;
        this.classID = classID;
        this.trainerID = trainerID;
        this.code = code;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(String trainerID) {
        this.trainerID = trainerID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
