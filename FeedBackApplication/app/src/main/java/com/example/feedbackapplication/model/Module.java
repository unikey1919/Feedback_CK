package com.example.feedbackapplication.model;

public class Module {
    private int ModuleID;
    private String AdminID;
    private String ModuleName;

    public Module(){

    }

    public Module(int moduleID, String adminID, String moduleName) {
        ModuleID = moduleID;
        AdminID = adminID;
        ModuleName = moduleName;
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
}
