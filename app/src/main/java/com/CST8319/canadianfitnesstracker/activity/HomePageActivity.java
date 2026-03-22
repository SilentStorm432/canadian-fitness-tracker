package com.CST8319.canadianfitnesstracker.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.CST8319.canadianfitnesstracker.MainActivity;
import com.CST8319.canadianfitnesstracker.R;

public class HomePageActivity extends AppCompatActivity {

    Button userButton;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        userButton = findViewById(R.id.userButton);
        logoutButton = findViewById(R.id.logoutButton);

        String username = getIntent().getStringExtra("username");

        if (username != null) {
            userButton.setText("Logged in as: " + username);
        }

        Button logWorkoutButton = findViewById(R.id.logWorkoutButton);
        Button profileButton = findViewById(R.id.viewProfile);

        logWorkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, LogWorkoutActivity.class);
            startActivity(intent);
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ProfilePageActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {

            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}