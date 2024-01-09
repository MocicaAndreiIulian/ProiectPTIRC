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

public class ViewPasswordActivity extends AppCompatActivity {

    private String userName, password;

    private TextView passName,passValue, passEdit, passTitle,passEmpty;

    private Button deletePass,savePass,generatePass;

    private EditText updatedPassword;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_password);

        DB = new DBHelper(this);

        passName = findViewById(R.id.passName);
        passValue = findViewById(R.id.passValue);
        deletePass = findViewById(R.id.deletePass);
        passEdit = findViewById(R.id.passEdit);
        passTitle = findViewById(R.id.passTitle);
        savePass = findViewById(R.id.saveNewPass);
        generatePass = findViewById(R.id.generateUpdatePass);
        updatedPassword = findViewById(R.id.updatedPassword);
        passEmpty = findViewById(R.id.passwordEmptyError);

        HashMap<String, String> receivedDataMap = (HashMap<String, String>) getIntent().getSerializableExtra("data");

        if(receivedDataMap == null) {
            return;
        }

        String passName = receivedDataMap.get("selectedPasswordName");
        String pass = receivedDataMap.get("selectedPassword");
        this.userName = receivedDataMap.get("user");
        this.password = receivedDataMap.get("pass");

        this.passName.setText(passName);
        this.passValue.setText(pass);

        deletePass.setOnClickListener(view -> {
            DB.deletePassword(passName,this.userName);
            Intent intent = new Intent(ViewPasswordActivity.this,HomeActivity.class);
            intent.putExtra("data",receivedDataMap);
            startActivity(intent);
            Toast.makeText(ViewPasswordActivity.this, "Password Deleted", Toast.LENGTH_SHORT).show();
        });

        passEdit.setOnClickListener(view -> {
            passTitle.setText("Type the new pass: ");
            passEdit.setVisibility(View.GONE);
            passValue.setVisibility(View.GONE);
            savePass.setVisibility(View.VISIBLE);
            generatePass.setVisibility(View.VISIBLE);
            updatedPassword.setVisibility(View.VISIBLE);
        });

        generatePass.setOnClickListener(view -> {
            String newPass = GeneratePassword.generateRandomPassword();
            updatedPassword.setText(newPass);
        });

        savePass.setOnClickListener(view -> {

            String newPass = updatedPassword.getText().toString();

            if (newPass.equals("")) {
                Messages.showMessage("Password should not be empty",passEmpty);
                return;
            }

            String cryptedPass = "";

            try {
                cryptedPass = Crypt.encryptPassword(newPass,this.password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            DB.updatePassword(cryptedPass,passName,this.userName);

            Intent intent = new Intent(ViewPasswordActivity.this,HomeActivity.class);
            intent.putExtra("data",receivedDataMap);
            startActivity(intent);
            Toast.makeText(ViewPasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
        });


    }

    public void copyPassToClipboard(View view) {

        String textToCopy = passValue.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);

        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}