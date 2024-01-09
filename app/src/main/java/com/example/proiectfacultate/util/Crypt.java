package com.example.proiectfacultate.util;

import android.util.Base64;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {

    public static String encryptPassword(String password, String userKey) throws Exception {

        byte[] salt = new SecureRandom().generateSeed(8);

        KeySpec spec = new PBEKeySpec(userKey.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new SecureRandom().generateSeed(16);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] encryptedPassword = cipher.doFinal(password.getBytes());

        byte[] combined = new byte[salt.length + iv.length + encryptedPassword.length];
        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(iv, 0, combined, salt.length, iv.length);
        System.arraycopy(encryptedPassword, 0, combined, salt.length + iv.length, encryptedPassword.length);

        return Base64.encodeToString(combined, Base64.DEFAULT);
    }

    public static String decryptPassword(String encryptedPassword, String userKey) throws Exception {

        byte[] combined = Base64.decode(encryptedPassword, Base64.DEFAULT);

        byte[] salt = Arrays.copyOfRange(combined, 0, 8);
        byte[] iv = Arrays.copyOfRange(combined, 8, 24);
        byte[] encryptedPasswordBytes = Arrays.copyOfRange(combined, 24, combined.length);

        KeySpec spec = new PBEKeySpec(userKey.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedPasswordBytes = cipher.doFinal(encryptedPasswordBytes);

        return new String(decryptedPasswordBytes);
    }

}
