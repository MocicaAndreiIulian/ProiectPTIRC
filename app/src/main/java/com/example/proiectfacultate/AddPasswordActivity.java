package com.example.proiectfacultate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiectfacultate.util.Crypt;
import com.example.proiectfacultate.util.GeneratePassword;
import com.example.proiectfacultate.util.Messages;

import java.util.HashMap;

public class AddPasswordActivity extends AppCompatActivity {

    private Button savePassword,generatePassword;

    private EditText passwordName, newPass;

    private DBHelper DB;

    private TextView messageTextView, copyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        DB = new DBHelper(this);

        passwordName = findViewById(R.id.passwordName);
        newPass = findViewById(R.id.newPass);

        messageTextView = findViewById(R.id.problemMessage);

        savePassword = findViewById(R.id.savePassword);
        generatePassword = findViewById(R.id.generatePassword);

        copyButton = findViewById(R.id.copyButton);

        copyButton.setOnClickListener(this::copyToClipboard);

        savePassword.setOnClickListener(view -> {
            String pass = newPass.getText().toString();
            String passName = passwordName.getText().toString();

            if(passName.equals("") || pass.equals("")) {
                Messages.showMessage("Please complete all the field",messageTextView);
                return;
            }

            HashMap<String, String> receivedDataMap = (HashMap<String, String>) getIntent().getSerializableExtra("data");

            if(receivedDataMap == null) {
                return;
            }
            String key = receivedDataMap.get("pass");
            String userName = receivedDataMap.get("user");

            try {
                String encryptedPass = Crypt.encryptPassword(pass,key);
                if(!DB.addPassword(passName,encryptedPass,userName)){
                    Messages.showMessage("A password with this name already exists",messageTextView);
                }else {
                    Intent intent = new Intent(AddPasswordActivity.this,HomeActivity.class);
                    intent.putExtra("data",receivedDataMap);
                    startActivity(intent);
                    Toast.makeText(AddPasswordActivity.this, "Password Saved", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        generatePassword.setOnClickListener(view -> {
            newPass.setText(GeneratePassword.generateRandomPassword());
        });

    }

    public void copyToClipboard(View view) {

        String textToCopy = newPass.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);

        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}