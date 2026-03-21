package com.CST8319.canadianfitnesstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import com.CST8319.canadianfitnesstracker.activity.Profile;
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
//changed to int
    public int verifyUser(String username, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM User WHERE Username=? AND Password=?";
        String[] selectionArgs = {username, password};
        android.database.Cursor cursor = db.rawQuery(query, selectionArgs);

        //edited out the boolean method replaced it with the user id so user id can be passed onto other methods
        //boolean exists = false;
         int userID = -1;

        if(cursor.moveToFirst()){
            //exists = true;
            userID = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
        }

        cursor.close();
        db.close();

        return userID;
    }

    public android.database.Cursor getAllWorkouts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Name FROM Workout", null);
    }

    //method to retrieve data from db used in PPA -Alejandro
    public Profile getUserByID(int userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                "SELECT u.user_id, u.Username, u.Password, u.Sex, u.Height, uw.Weight " +
                        "FROM User u " +
                        "LEFT JOIN UserWeight uw ON u.user_id = uw.UserID " +
                        "WHERE u.user_id = ? " +
                        "ORDER BY uw.ID DESC " +
                        "LIMIT 1";

        android.database.Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        Profile profile = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("Username"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
            String sex = cursor.getString(cursor.getColumnIndexOrThrow("Sex"));
            int height = cursor.getInt(cursor.getColumnIndexOrThrow("Height"));

            int weight = 0;
            int weightIndex = cursor.getColumnIndex("Weight");
            if (weightIndex != -1 && !cursor.isNull(weightIndex)) {
                weight = cursor.getInt(weightIndex);
            }

            double bmi = 0;
            if (height > 0 && weight > 0) {
                double heightMeters = height / 100.0;
                bmi = weight / (heightMeters * heightMeters);
            }

            profile = new Profile(id, username, password, sex, height, weight, bmi);
        }

        cursor.close();
        db.close();

        return profile;


    }


}