package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

import java.util.ArrayList;

public class ViewWorkoutsActivity extends AppCompatActivity {

    ListView sessionListView;
    DBHelper dbHelper;
    ArrayList<Integer> sessionIDs = new ArrayList<>();
    ArrayList<String> sessionNotes = new ArrayList<>();
    ArrayList<String> sessionTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);

        sessionListView = findViewById(R.id.sessionListView);
        dbHelper = new DBHelper(this);

        loadSessions();

        sessionListView.setOnItemClickListener((parent, view, position, id) -> {

            int selectedSessionID = sessionIDs.get(position);
            String title = sessionTitles.get(position);
            String notes = sessionNotes.get(position);

            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage("Notes:\n" + (notes != null ? notes : "No notes"))
                    .setPositiveButton("Delete", (dialog, which) -> {

                        boolean success = dbHelper.deleteSession(selectedSessionID);

                        if (success) {
                            Toast.makeText(this, "Session deleted", Toast.LENGTH_SHORT).show();
                            loadSessions(); // refresh list
                        } else {
                            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Close", null)
                    .show();
        });
    }

    private void loadSessions() {

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = prefs.getInt("userID", -1);

        Cursor cursor = dbHelper.getSessionsByUser(userID);

        ArrayList<String> sessions = new ArrayList<>();
        sessionIDs.clear();
        sessionNotes.clear();
        sessionTitles.clear();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String time = cursor.getString(2);
                int duration = cursor.getInt(3);
                int sets = cursor.getInt(4);
                int calories = cursor.getInt(5);
                String notes = cursor.getString(6);
                String workoutName = cursor.getString(7);

                sessionIDs.add(id);
                sessionNotes.add(notes);
                sessionTitles.add(workoutName);

                String display = workoutName +
                        "\n" + date + " " + time +
                        "\nDuration: " + duration + " min, Sets: " + sets;

                sessions.add(display);

            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                sessions
        );

        sessionListView.setAdapter(adapter);
    }
}