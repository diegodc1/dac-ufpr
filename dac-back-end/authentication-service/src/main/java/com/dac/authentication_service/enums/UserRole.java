package com.dac.authentication_service.enums;

public enum UserRole {
    EMPLOYEE("FUNCIONARIO"),
    CLIENT("CLIENTE");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
