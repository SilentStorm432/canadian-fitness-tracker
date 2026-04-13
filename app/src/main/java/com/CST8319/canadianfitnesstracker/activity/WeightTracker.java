package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.graph.CustomGraphView;
import com.CST8319.canadianfitnesstracker.repository.ProfileRepository;

import java.util.concurrent.atomic.AtomicLong;

public class WeightTracker extends AppCompatActivity {

    private ProfileRepository profileRepository;
    //had a non -static error thing pop up
    private CustomGraphView graphView;
    int weight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // doing some googling I searched how to make a graph apparetly I will need to use a new custom view if I dont want to use a library
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = prefs.getInt("userID", -1);
        setContentView(R.layout.activity_weight_tracker);
        //added this to stop crasshes due to thing not initialized before setting the height
        graphView = findViewById(R.id.weightGraph);

        loadProfileData(userID);

    }
    private void loadProfileData(int userID) {
        profileRepository = new ProfileRepository(this);
        Profile profile = profileRepository.getUserId(userID);
        weight = Integer.parseInt(profile.getWeight());
        graphView.setHeight(weight);

    }

}
