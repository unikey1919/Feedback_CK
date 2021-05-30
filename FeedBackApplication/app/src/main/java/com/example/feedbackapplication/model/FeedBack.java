package com.example.feedbackapplication.model;

public class FeedBack {
    private int FeedbackID;
    private String Title;
    private String AdminID;
    private String TypeFeedback;

    public FeedBack() {

    }

    public FeedBack(int feedbackID, String title, String adminID, String typeFeedback) {
        FeedbackID = feedbackID;
        Title = title;
        AdminID = adminID;
        TypeFeedback = typeFeedback;
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

    public String getTypeFeedback() {
        return TypeFeedback;
    }

    public void setTypeFeedback(String typeFeedback) {
        TypeFeedback = typeFeedback;
    }
}
