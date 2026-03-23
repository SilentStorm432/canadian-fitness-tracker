package com.CST8319.canadianfitnesstracker.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

import java.util.ArrayList;

public class LogWorkoutActivity extends AppCompatActivity {

    ListView workoutListView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        workoutListView = findViewById(R.id.workoutListView);
        dbHelper = new DBHelper(this);

        loadWorkouts();
    }

    private void loadWorkouts() {

        Cursor cursor = dbHelper.getAllWorkouts();
        ArrayList<String> workouts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                workouts.add(cursor.getString(0)); // Name column
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                workouts
        );

        workoutListView.setAdapter(adapter);
    }
}