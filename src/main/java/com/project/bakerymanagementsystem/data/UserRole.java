package com.project.bakerymanagementsystem.data;


public enum UserRole {
    ADMIN("Admin");

    String roleText;

    UserRole(String roleText) {
        this.roleText = roleText;
    }

    public String getRoleText() {
        return this.roleText;
    }
}
