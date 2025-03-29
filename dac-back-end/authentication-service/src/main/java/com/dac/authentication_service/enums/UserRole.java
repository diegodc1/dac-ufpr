package com.dac.authentication_service.enums;

public enum UserRole {
    EMPLOYEE("EMPLOYEE"),
    CLIENT("CLIENT");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
