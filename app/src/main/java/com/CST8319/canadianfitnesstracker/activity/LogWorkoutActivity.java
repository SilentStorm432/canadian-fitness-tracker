package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LogWorkoutActivity extends AppCompatActivity {

    ListView workoutListView;
    DBHelper dbHelper;
    ArrayList<Integer> workoutIDs = new ArrayList<>();   // moved here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_workout);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = prefs.getInt("userID", -1);

        workoutListView = findViewById(R.id.workoutListView);
        dbHelper = new DBHelper(this);

        loadWorkouts();

        workoutListView.setOnItemClickListener((parent, view, position, id) -> {

            int selectedWorkoutID = workoutIDs.get(position);

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_logworkout, null);

            EditText durationInput = dialogView.findViewById(R.id.inputDuration);
            EditText setsInput = dialogView.findViewById(R.id.inputSets);
            EditText caloriesInput = dialogView.findViewById(R.id.inputCalories);
            EditText notesInput = dialogView.findViewById(R.id.inputNotes);

            new AlertDialog.Builder(this)
                    .setTitle("Log Workout")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {

                        if (userID == -1) {
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //get values
                        int duration = Integer.parseInt(durationInput.getText().toString());
                        int sets = Integer.parseInt(setsInput.getText().toString());
                        int calories = Integer.parseInt(caloriesInput.getText().toString());
                        String notes = notesInput.getText().toString();
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault())
                                .format(new Date());

                        boolean success = dbHelper.addSession(
                                currentDate,
                                currentTime,
                                duration,
                                sets,
                                userID,
                                selectedWorkoutID,
                                calories,
                                notes
                        );

                        if (success) {
                            Toast.makeText(this, "Session logged!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to log session", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void loadWorkouts() {

        Cursor cursor = dbHelper.getAllWorkouts();
        ArrayList<String> workouts = new ArrayList<>();
        workoutIDs.clear();

        if (cursor.moveToFirst()) {
            do {
                workoutIDs.add(cursor.getInt(0));
                workouts.add(cursor.getString(1));
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