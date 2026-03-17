package com.CST8319.canadianfitnesstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import com.CST8319.canadianfitnesstracker.database.WorkoutData;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable =
                "CREATE TABLE User (" +
                        "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Username TEXT UNIQUE," +
                        "Password TEXT," +
                        "Sex TEXT," +
                        "Height INTEGER)";

        String createWorkoutTable =
                "CREATE TABLE Workout (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Name TEXT, " +
                        "Description TEXT" +
                        ");";

        String createSessionTable =
                "CREATE TABLE Session (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Date TEXT, " +
                        "Time TEXT, " +
                        "Duration INTEGER, " +
                        "Sets INTEGER, " +
                        "UserID INTEGER, " +
                        "WorkoutID INTEGER, " +
                        "CaloriesBurned INTEGER, " +
                        "Notes TEXT, " +
                        "FOREIGN KEY(UserID) REFERENCES User(ID), " +
                        "FOREIGN KEY(WorkoutID) REFERENCES Workout(ID)" +
                        ");";

        String createUserWeightTable =
                "CREATE TABLE UserWeight (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "UserID INTEGER, " +
                        "Date TEXT, " +
                        "Weight INTEGER, " +
                        "FOREIGN KEY(UserID) REFERENCES User(ID)" +
                        ");";

        db.execSQL(createUserTable);
        db.execSQL(createWorkoutTable);
        db.execSQL(createSessionTable);
        db.execSQL(createUserWeightTable);

        // Insert workouts
        for (String[] workout : WorkoutData.WORKOUTS) {

            ContentValues values = new ContentValues();
            values.put("Name", workout[0]);
            values.put("Description", workout[1]);

            db.insert("Workout", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Session");
        db.execSQL("DROP TABLE IF EXISTS UserWeight");
        db.execSQL("DROP TABLE IF EXISTS Workout");
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);

    }

    public void addWorkout(String name, String description) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Description", description);

        db.insert("Workout", null, values);
    }
    public long addUser(String username, String password, int height, String sex) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Height", height);
        values.put("Sex", sex);
        values.put("Username", username);
        values.put("Password", password);


        long result = db.insert("User", null, values);

        db.close();

        return result;
    }

    public boolean verifyUser(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM User WHERE Username=? AND Password=?";
        String[] selectionArgs = {username, password};
        android.database.Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = false;

        if(cursor.moveToFirst()){
            exists = true;
        }

        cursor.close();
        db.close();

        return exists;
    }

    public android.database.Cursor getAllWorkouts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Name FROM Workout", null);
    }

}