package com.example.feedbackapplication.model;

public class Module {
    private int ModuleID;
    private String AdminID;
    private String ModuleName;
    private String StartDate;
    private String EndDate;
    private String FeedbackTitle;
    private String FeedbackStartDate;
    private String FeedbackEndDate;


    public Module(){

    }

    public Module(int moduleID, String adminID, String moduleName, String startDate, String endDate, String feedbackTitle, String feedbackStartDate, String feedbackEndDate) {
        ModuleID = moduleID;
        AdminID = adminID;
        ModuleName = moduleName;
        StartDate = startDate;
        EndDate = endDate;
        FeedbackTitle = feedbackTitle;
        FeedbackStartDate = feedbackStartDate;
        FeedbackEndDate = feedbackEndDate;
    }

    public int getModuleID() {
        return ModuleID;
    }

    public void setModuleID(int moduleID) {
        ModuleID = moduleID;
    }

    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String adminID) {
        AdminID = adminID;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
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

    public String getFeedbackTitle() {
        return FeedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        FeedbackTitle = feedbackTitle;
    }

    public String getFeedbackStartDate() {
        return FeedbackStartDate;
    }

    public void setFeedbackStartDate(String feedbackStartDate) {
        FeedbackStartDate = feedbackStartDate;
    }

    public String getFeedbackEndDate() {
        return FeedbackEndDate;
    }

    public void setFeedbackEndDate(String feedbackEndDate) {
        FeedbackEndDate = feedbackEndDate;
    }
}
