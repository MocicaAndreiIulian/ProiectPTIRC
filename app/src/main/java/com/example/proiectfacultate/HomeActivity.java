package com.example.proiectfacultate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proiectfacultate.homeRecycle.Adapter;
import com.example.proiectfacultate.util.Crypt;
import com.example.proiectfacultate.util.Password;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private String userName, password;
    private Button addPasswordButton;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DBHelper(this);

        HashMap<String, String> receivedDataMap = (HashMap<String, String>) getIntent().getSerializableExtra("data");

        if (receivedDataMap != null) {
            this.userName = receivedDataMap.get("user");
            this.password = receivedDataMap.get("pass");
        }

        addPasswordButton = findViewById(R.id.buttonAddPassword);


        addPasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this,AddPasswordActivity.class);
            intent.putExtra("data",receivedDataMap);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        Map<Integer, Password> passwordMap = dbHelper.getAllPassword();

        List<String> nameList = new ArrayList<>();
        passwordMap.forEach((k,v) -> nameList.add(v.getPasswordName()));

        Adapter adapter = new Adapter(nameList);
        recyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(position -> {

            HashMap<String, String> data = new HashMap<>(receivedDataMap);
            String selectedPassword = Crypt.decryptPassword(passwordMap.get(position).getPassword(),this.password);
            String selectedPasswordName = passwordMap.get(position).getPasswordName();
            data.put("selectedPassword",selectedPassword);
            data.put("selectedPasswordName",selectedPasswordName);
            Intent intent = new Intent(HomeActivity.this,ViewPasswordActivity.class);
            intent.putExtra("data",data);
            startActivity(intent);
        });
    }
}