package com.example.proiectfacultate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private Button btnLogin,btnSingup;

    private DBHelper DB;

    private final HashMap<String, String> data = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);

        btnLogin = (Button) findViewById(R.id.btnsingin1);
        btnSingup = (Button) findViewById(R.id.btnsignup1);

        DB = new DBHelper(this);

        btnLogin.setOnClickListener(view -> {

            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.equals("")||pass.equals("")) {
                Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            }else {
                Boolean checkUserPass = DB.checkUsernamePassword(user,pass);
                if (checkUserPass) {
                    Toast.makeText(LoginActivity.this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    data.put("user",user);
                    data.put("pass",pass);
                    intent.putExtra("data",data);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSingup.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SingupActivity.class);
            startActivity(intent);
        });


    }

}
