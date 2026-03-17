package com.CST8319.canadianfitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.CST8319.canadianfitnesstracker.activity.HomePageActivity;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

public class MainActivity extends AppCompatActivity {

    EditText editUsername;
    EditText editPassword;
    Button loginButton;
    Button createAccountButton;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }

        // UI
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        // start database
        dbHelper = new DBHelper(this);

        loginButton.setOnClickListener(view -> {

            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();

            boolean validUser = dbHelper.verifyUser(username, password);

            if(validUser){
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                getSharedPreferences("UserSession", MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .apply();

                Intent intentHome = new Intent(MainActivity.this, HomePageActivity.class);
                intentHome.putExtra("username", username);
                startActivity(intentHome);
                finish();

            } else {
                Toast.makeText(MainActivity.this,"Invalid username or password",Toast.LENGTH_SHORT).show();
            }

        });

        createAccountButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, com.CST8319.canadianfitnesstracker.activity.RegisterActivity.class);
            startActivity(intent);

        });

    }
}