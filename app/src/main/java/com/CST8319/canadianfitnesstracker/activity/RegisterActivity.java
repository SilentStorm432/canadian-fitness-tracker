package com.CST8319.canadianfitnesstracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText height;
    EditText sex;

    Button registerButton;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        height = findViewById(R.id.registerHeight);
        sex = findViewById(R.id.registerSex);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DBHelper(this);

        registerButton.setOnClickListener(view -> {

            String user = username.getText().toString();
            String pass = password.getText().toString();
            String userSex = sex.getText().toString();
            int userHeight = Integer.parseInt(height.getText().toString());

            long result = dbHelper.addUser(user, pass, userHeight, userSex);

            if(result != -1){
                Toast.makeText(RegisterActivity.this,"User created",Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(RegisterActivity.this,"error making user profile",Toast.LENGTH_SHORT).show();
            }

        });

    }
}