package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

public class ProfilePageActivity extends AppCompatActivity {

    private EditText profileUsername;
    private EditText profilePassword;
    private EditText profileHeight;
    private EditText profileSex;
    private EditText profileWeight;
    private EditText profileBMI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = prefs.getInt("userID", -1);

        setContentView(R.layout.activity_profile_page);

        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePasword);
        profileHeight = findViewById(R.id.profileHeight);
        profileSex = findViewById(R.id.profileSex);
        profileWeight = findViewById(R.id.profileWeight);
        profileBMI  = findViewById(R.id.profileBMI);

        loadProfileData(userID);

    }
private void loadProfileData(int userID)
{
    String username = "Jeffy";
    String password = "Mishima";
    String height = "200";
    String sex = "Male";
    String weight = "90";
    String BMI = "1";

    DBHelper dbHelper = new DBHelper(this);
    Profile profile = dbHelper.getUserByID(userID); // example user id

    if (profile != null)
    {
        profileUsername.setText(profile.getUsername());
        profilePassword.setText(profile.getPassword());
        profileHeight.setText(profile.getHeight());
        profileSex.setText(profile.getSex());
        profileWeight.setText(profile.getWeight());
        profileBMI.setText(profile.getBMI());
    }
    else
    {
        profileUsername.setText(username);
        profilePassword.setText(password);
        profileHeight.setText(height);
        profileSex.setText(sex);
        profileWeight.setText(weight);
        profileBMI.setText(BMI);
    }


}


}
