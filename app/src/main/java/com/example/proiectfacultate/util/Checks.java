package com.example.proiectfacultate.util;

public class Checks {

    public static boolean checkIfPasswordIsProtected(String pass) {

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,128}$";

        return pass.matches(regex);
    }
}
