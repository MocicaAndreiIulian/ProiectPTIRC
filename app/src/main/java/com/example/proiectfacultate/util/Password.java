package com.example.proiectfacultate.util;

public class Password {

    private String passwordName;

    private String password;

    public Password(String passwordName, String password) {
        this.passwordName = passwordName;
        this.password = password;
    }

    public String getPasswordName() {
        return passwordName;
    }

    public String getPassword() {
        return password;
    }
}
