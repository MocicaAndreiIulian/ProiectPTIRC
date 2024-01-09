package com.example.proiectfacultate.util;

import org.mindrot.jbcrypt.BCrypt;

public class Hash {


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verifyPassword(String enteredPassword, String hashedPassword) {
        return BCrypt.checkpw(enteredPassword, hashedPassword);
    }
}
