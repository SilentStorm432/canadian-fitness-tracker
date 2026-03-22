package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button saveProfile;

    private String originalUsername = "";
    private String originalPassword = "";
    private String originalHeight = "";
    private String originalSex = "";
    private String originalWeight = "";
    private String originalBMI = "";

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
        saveProfile = findViewById(R.id.saveProfile);

        saveProfile.setEnabled(false);

        loadProfileData(userID);
        changeListener();

        saveProfile.setOnClickListener(v -> saveProfileData(userID));

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

    originalUsername = profileUsername.getText().toString();
    originalPassword = profilePassword.getText().toString();
    originalHeight = profileHeight.getText().toString();
    originalSex = profileSex.getText().toString();
    originalWeight = profileWeight.getText().toString();
    originalBMI = profileBMI.getText().toString();

    saveProfile.setEnabled(false);
}

private void changeListener()
{
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {

        }

        //check changes was initially here which caused a bit of confusion as I couldnt figure out why only the button would become available after two changes
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            checkChanges();

        }
    };

    profileUsername.addTextChangedListener(watcher);
    profilePassword.addTextChangedListener(watcher);
    profileHeight.addTextChangedListener(watcher);
    profileSex.addTextChangedListener(watcher);
    profileWeight.addTextChangedListener(watcher);
    profileBMI.addTextChangedListener(watcher);
}


private void checkChanges()
{
    if (!profileUsername.getText().toString().equals(originalUsername) ||
            !profilePassword.getText().toString().equals(originalPassword) ||
            !profileHeight.getText().toString().equals(originalHeight) ||
            !profileSex.getText().toString().equals(originalSex) ||
            !profileWeight.getText().toString().equals(originalWeight) ||
            !profileBMI.getText().toString().equals(originalBMI))
    {
        saveProfile.setEnabled(true);
    }
}

public void saveProfileData(int userID)
{
    String username = profileUsername.getText().toString().trim();
    String password = profilePassword.getText().toString().trim();
    String sex = profileSex.getText().toString().trim();

    int height;
    int weight;

    try {
        height = Integer.parseInt(profileHeight.getText().toString().trim());
        weight = Integer.parseInt(profileWeight.getText().toString().trim());
    } catch (NumberFormatException e) {
        Toast.makeText(this, "height and text must be whole numbers", Toast.LENGTH_SHORT).show();
        return;
    }

    DBHelper dbHelper = new DBHelper(this);

    boolean userUpdated = dbHelper.updateProfile(userID, username, password, sex, height);
    boolean weightUpdated = dbHelper.saveWeight(userID, weight);

    if (userUpdated && weightUpdated) {
        double bmi = 0;
        if (height > 0 && weight > 0) {
        }

        originalUsername = profileUsername.getText().toString();
        originalPassword = profilePassword.getText().toString();
        originalHeight = profileHeight.getText().toString();
        originalSex = profileSex.getText().toString();
        originalWeight = profileWeight.getText().toString();
        originalBMI = profileBMI.getText().toString();

        saveProfile.setEnabled(false);

        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
    }

}

}
