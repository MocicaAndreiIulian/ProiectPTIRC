package com.example.proiectfacultate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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

    private TextView passName,passValue, passEdit, passTitle,passEmpty,passCopy;

    private Button deletePass,savePass,generatePass,homePageButton,stopEditButton;

    private EditText updatedPassword;
    private DBHelper DB;

    HashMap<String, String> data;

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
        passCopy = findViewById(R.id.CopyPasswordViewPass);
        homePageButton = findViewById(R.id.buttonHomePage);
        stopEditButton = findViewById(R.id.buttonStopEditing);

        this.data = (HashMap<String, String>) getIntent().getSerializableExtra("data");
        
        if(this.data == null) {
            return;
        }

        String passName = this.data.get("selectedPasswordName");
        String pass = this.data.get("selectedPassword");
        this.userName = this.data.get("user");
        this.password = this.data.get("pass");

        this.passName.setText(passName);
        this.passValue.setText(pass);

//        deletePass.setOnClickListener(view -> {
//            DB.deletePassword(passName,this.userName);
//            Intent intent = new Intent(ViewPasswordActivity.this,HomeActivity.class);
//            intent.putExtra("data",this.data);
//            startActivity(intent);
//            Toast.makeText(ViewPasswordActivity.this, "Password Deleted", Toast.LENGTH_SHORT).show();
//        });

        passEdit.setOnClickListener(view -> {
            passTitle.setText("Type the new pass: ");
            passCopy.setVisibility(View.GONE);
            passEdit.setVisibility(View.GONE);
            passValue.setVisibility(View.GONE);
            savePass.setVisibility(View.VISIBLE);
            generatePass.setVisibility(View.VISIBLE);
            updatedPassword.setVisibility(View.VISIBLE);
            stopEditButton.setVisibility(View.VISIBLE);
            updatedPassword.setText(pass);
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
            intent.putExtra("data",this.data);
            startActivity(intent);
            Toast.makeText(ViewPasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
        });

        homePageButton.setOnClickListener(view -> {
            Intent intent = new Intent(ViewPasswordActivity.this,HomeActivity.class);
            intent.putExtra("data",this.data);
            startActivity(intent);
        });

        stopEditButton.setOnClickListener(view -> {
            passTitle.setText("Password: ");
            passCopy.setVisibility(View.VISIBLE);
            passEdit.setVisibility(View.VISIBLE);
            passValue.setVisibility(View.VISIBLE);
            savePass.setVisibility(View.GONE);
            generatePass.setVisibility(View.GONE);
            updatedPassword.setVisibility(View.GONE);
            stopEditButton.setVisibility(View.GONE);
        });


    }

    public void copyPassToClipboard(View view) {

        String textToCopy = passValue.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("Copied Text", textToCopy);

        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    public void showConfirmationDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this password?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DB.deletePassword(data.get("selectedPasswordName"),userName);
                    Intent intent = new Intent(ViewPasswordActivity.this,HomeActivity.class);
                    intent.putExtra("data",data);
                    startActivity(intent);
                    Toast.makeText(ViewPasswordActivity.this, "Password Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> {})
                .show();
    }
}