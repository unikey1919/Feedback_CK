package com.example.feedbackapplication.model;

public class Trainee {
    private String UserName;
    private String Name;
    private String Email;
    private String Phone;
    private String Address;
    private int isActive;
    private String PassWord;
    private String ActivationCode;
    private String ResetPasswordCode;

    public Trainee(){

    }

    public Trainee(String userName, String name, String email, String phone, String address, int isActive, String passWord, String activationCode, String resetPasswordCode) {
        UserName = userName;
        Name = name;
        Email = email;
        Phone = phone;
        Address = address;
        this.isActive = isActive;
        PassWord = passWord;
        ActivationCode = activationCode;
        ResetPasswordCode = resetPasswordCode;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getActivationCode() {
        return ActivationCode;
    }

    public void setActivationCode(String activationCode) {
        ActivationCode = activationCode;
    }

    public String getResetPasswordCode() {
        return ResetPasswordCode;
    }

    public void setResetPasswordCode(String resetPasswordCode) {
        ResetPasswordCode = resetPasswordCode;
    }
}
