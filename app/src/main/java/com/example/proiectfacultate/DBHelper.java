package com.example.proiectfacultate;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.proiectfacultate.util.Hash;
import com.example.proiectfacultate.util.Password;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "DB.db";
    private final SQLiteDatabase MyDB = this.getWritableDatabase();

    public static final String USERS = "users";
    public static final String PASSWORDS = "passwords";

    public DBHelper(Context context) {
        super(context, DBNAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table users (username TEXT primary key, password TEXT)");
        sqLiteDatabase.execSQL("create Table passwords(name TEXT primary key, password TEXT, user_name Text, FOREIGN KEY(user_name) REFERENCES users(username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
        sqLiteDatabase.execSQL("drop Table if exists passwords");
    }

    public Boolean addUser(String username, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password", Hash.hashPassword(password));
        long result = MyDB.insert(DBHelper.USERS,null, contentValues);
        return result != -1;
    }

    public Boolean checkUsername(String username) {
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?",new String[]{username});
        boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }

    public Boolean checkUsernamePassword(String username, String password) {
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        String passwordValue = "";
        if (cursor != null && cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("password");

            // Retrieve the value from the "password" column
            passwordValue = cursor.getString(passwordColumnIndex);
            cursor.close();
        }
        return Hash.verifyPassword(password,passwordValue);
    }

    public Boolean addPassword(String passwordName,String pass, String userName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",passwordName);
        contentValues.put("password",pass);
        contentValues.put("user_name",userName);
        long result = MyDB.insert(DBHelper.PASSWORDS,null,contentValues);
        return result != -1;
    }


    public Boolean deletePassword(String passwordName, String userName) {

        String selection = "name = ? AND user_name = ?";
        String[] selectionArgs = {passwordName, userName};

        long result = MyDB.delete(DBHelper.PASSWORDS,selection,selectionArgs);

        return result != -1;
    }

    public Map<Integer, Password> getAllPassword() {

        Map<Integer,Password> map = new HashMap<>();

        Cursor cursor = MyDB.rawQuery("Select * from " + DBHelper.PASSWORDS,null);

        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String passwordName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                map.put(i++,new Password(passwordName,password));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return map;
    }

    public boolean updatePassword(String newPass, String passName, String userName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",newPass);

        String selection = "name = ? AND user_name = ?";
        String[] selectionArgs = {passName, userName};

        int rowsUpdated = MyDB.update("passwords", contentValues, selection, selectionArgs);

        MyDB.close();

        return rowsUpdated != -1;
    }

}
