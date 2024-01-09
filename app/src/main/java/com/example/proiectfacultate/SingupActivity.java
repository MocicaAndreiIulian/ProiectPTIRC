package com.example.proiectfacultate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiectfacultate.util.Checks;
import com.example.proiectfacultate.util.Hash;
import com.example.proiectfacultate.util.Messages;

import java.util.HashMap;
import java.util.Map;

public class SingupActivity extends AppCompatActivity {

    private EditText username, password, repassword;
    private Button singup, signin;

    private final HashMap<String, String> data = new HashMap<>();

    private DBHelper DB;

    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        singup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsingin);

        messageTextView = findViewById(R.id.messageTextView);


        DB = new DBHelper(this);

        singup.setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();

            if(user.equals("") || pass.equals("")||repass.equals("")) {
                Toast.makeText(SingupActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            }else {
                if (pass.equals(repass)) {
                    if (Checks.checkIfPasswordIsProtected(pass)) {
                        Boolean checkUser = DB.checkUsername(user);
                        if (!checkUser) {
                            Boolean insert = DB.addUser(user, pass);
                            if (insert) {
                                Toast.makeText(SingupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SingupActivity.this, HomeActivity.class);
                                data.put("user",user);
                                data.put("pass",pass);
                                intent.putExtra("data",data);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SingupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SingupActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Messages.showMessage("Password is not secure, please use upper case,lower case, numbers, a special character and the length should be at least 9 characters!",messageTextView);
                    }
                } else {
                    Toast.makeText(SingupActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signin.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);

        });
    }

}