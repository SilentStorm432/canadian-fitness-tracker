package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.graph.CustomGraphView;
import com.CST8319.canadianfitnesstracker.repository.ProfileRepository;
import com.CST8319.canadianfitnesstracker.repository.WeightRepository;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

public class WeightTracker extends AppCompatActivity {

    private WeightRepository weightRapository;
    //had a non -static error thing pop up
    private CustomGraphView graphView;
    int weight;

    private EditText doYouKnowTheWeight;
    private Button saveWeight;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // doing some googling I searched how to make a graph apparetly I will need to use a new custom view if I dont want to use a library
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = prefs.getInt("userID", -1);
        setContentView(R.layout.activity_weight_tracker);
        //added this to stop crasshes due to thing not initialized before setting the height
        graphView = findViewById(R.id.weightGraph);
        doYouKnowTheWeight = findViewById(R.id.weightEntryText);
        saveWeight = findViewById(R.id.weightEntryButton);

        loadProfileData(userID);

        saveWeight.setOnClickListener(v -> saveProfileData(userID));

    }

    //friday change me to use WeightData and Weight repository-av to av

    private void loadProfileData(int userID) {
        for(int i = 0; i <7; i++)
        {
            LocalDate kyo = LocalDate.now().minusDays(i);

            weightRapository = new WeightRepository(this);
            Profile profile = weightRapository.getUserId(userID);
            WeightData weightData = weightRapository.getWeightData(userID, String.valueOf(kyo));

            weight = weightData.getweightAvg();

            graphView.setHeight(weight, 6-i);

        }
    }

    public void saveProfileData(int userID)
    {

        int weightEntry;

        // try catch was added because it let me add letters and stuff and that would have meesssed up data entry and I didnt wanted to find out how to only accept numbers so toast to the rescue-AV
        try {
            weightEntry = Integer.parseInt(doYouKnowTheWeight.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "height and text must be whole numbers", Toast.LENGTH_SHORT).show();
            return;
        }


        weightRapository = new WeightRepository(this);

        boolean weightUpdated = weightRapository.updateWeight(userID, weightEntry);

        if (weightUpdated)
        {
            loadProfileData(userID);
          Toast.makeText(this, "Weight updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }

    }



}