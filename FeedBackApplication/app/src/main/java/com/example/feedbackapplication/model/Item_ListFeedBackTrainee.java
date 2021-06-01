package com.example.feedbackapplication.model;

public class Item_ListFeedBackTrainee {
    private String image;
    private String txtFeedBackTitle;
    private String txtClassID;
    private String txtClassName;
    private String txtModuleID;
    private String txtModuleName;
    private String txtEndTime;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void settxtFeedBackTitle(String name) {
        this.txtFeedBackTitle = name;
    }

    public String gettxtFeedBackTitle() {
        return txtFeedBackTitle;
    }

    public void setDes(String des) {
        this.txtClassID = des;
    }

    public String gettxtClassID() {
        return txtClassID;
    }

    public void settxtClassName(String type) {
        this.txtClassName = type;
    }

    public String gettxtClassName() {
        return txtClassName;
    }

    public void settxtModuleID(String name2) {
        this.txtModuleID = name2;
    }

    public String gettxtModuleID() {
        return txtModuleID;
    }

    public void settxtModuleName(String des) {
        this.txtModuleName = txtModuleName;
    }

    public String gettxtModuleName() {
        return txtModuleName;
    }

    public void settxtEndTime(String type2) {
        this.txtEndTime = type2;
    }

    public String gettxtEndTime() {
        return txtEndTime;
    }

    public Item_ListFeedBackTrainee(){
    }

    public Item_ListFeedBackTrainee(String image, String txtFeedBackTitle, String txtClassID, String txtClassName, String txtModuleID, String txtModuleName, String txtEndTime){
        this.image=image;
        this.txtFeedBackTitle=txtFeedBackTitle;
        this.txtClassID=txtClassID;
        this.txtClassName=txtClassName;
        this.txtModuleID=txtModuleID;
        this.txtModuleName=txtModuleName;
        this.txtEndTime=txtEndTime;
    }


}
