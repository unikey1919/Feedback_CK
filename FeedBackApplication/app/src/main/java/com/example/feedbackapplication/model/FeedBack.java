package com.example.feedbackapplication.model;

public class FeedBack {
    private int FeedbackID;
    private String Title;
    private String AdminID;

    public FeedBack() {

    }

    public FeedBack(int feedbackID, String title, String adminID) {
        FeedbackID = feedbackID;
        Title = title;
        AdminID = adminID;
    }

    public int getFeedbackID() {
        return FeedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        FeedbackID = feedbackID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAdminID() {
        return AdminID;
    }

    public void setAdminID(String adminID) {
        AdminID = adminID;
    }

}
