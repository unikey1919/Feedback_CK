package com.example.feedbackapplication.model;

public class Admin {
    private String UserName;
    private String Name;
    private String Email;
    private String PassWord;

    public Admin(){

    }

    public Admin(String userName, String name, String email, String passWord) {
        UserName = userName;
        Name = name;
        Email = email;
        PassWord = passWord;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }
}
