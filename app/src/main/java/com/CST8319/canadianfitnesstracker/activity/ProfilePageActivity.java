package com.CST8319.canadianfitnesstracker.activity;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.CST8319.canadianfitnesstracker.R;
import com.CST8319.canadianfitnesstracker.repository.ProfileRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

public class ProfilePageActivity extends AppCompatActivity {

    private ProfileRepository profileRepository;

    private EditText profileUsername;
    private EditText profilePassword;
    private EditText profileHeight;
    private EditText profileSex;
    private EditText profileWeight;
    private TextView profileBMI;
    private Button saveProfile;
    private ImageView profileImg;

    private String profileImgUri ="";

    private String originalUsername = "";
    private String originalPassword = "";
    private String originalHeight = "";
    private String originalSex = "";
    private String originalWeight = "";
    private String originalBMI = "";
    private String originalImgUri="";

    private ActivityResultLauncher<String[]> imgPicker;

    private static final AtomicLong counter = new AtomicLong();

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
        profileImg = findViewById(R.id.profileImage);

        saveProfile.setEnabled(false);

        loadProfileData(userID);
        changeListener();

        saveProfile.setOnClickListener(v -> saveProfileData(userID));

        //source so I dont lose it  https://gist.github.com/mebjas/c44808589bb95ec288945b097dc2b687 and https://developer.android.com/training/basics/intents/result I dont fully get it yet but its kinda working
        //tutorial to followhttps://www.youtube.com/watch?v=nOtlFl1aUCw
        imgPicker = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri profPic)
                    {
                        // would break if deleted picture saving picture internally instead ... source to be doc in report https://gist.github.com/nikartx/79932c0a4f0a644f7ce020143146db98
                        if (profPic != null)
                        {
                            String imgPath = saveImg(profPic);
                            //turning the image into a bitmap to be saved
                            //https://www.youtube.com/watch?v=we_uBkZAS90
                            //https://developer.android.com/reference/android/graphics/BitmapFactory
                            profileImg.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                            profileImgUri = imgPath;
                            checkChanges();
                        }
                    }
                }
        );

        profileImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                imgPicker.launch(new String[]{"image/*"});
            }

        });

    }
private void loadProfileData(int userID)
{
    String username = "Jeffy";
    String password = "Mishima";
    String height = "200";
    String sex = "Male";

    //old methods replaced with repository-AV ... I should stanrize the userID/Id
    //DBHelper dbHelper = new DBHelper(this);
    //Profile profile = dbHelper.getUserByID(userID); // example user id

    profileRepository = new ProfileRepository(this);
    Profile profile = profileRepository.getUserId(userID);

    if (profile != null)
    {
        profileUsername.setText(profile.getUsername());
        profilePassword.setText(profile.getPassword());
        profileHeight.setText(profile.getHeight());
        profileSex.setText(profile.getSex());
        profileWeight.setText(profile.getWeight());
        profileBMI.setText(profile.getBMI());
        //the app kept crashing 1st becuase the database was old and did not have the column so had to reinstall the app
        //then it crashed because the img was empty and null when a new user was created so I added an if else
        String profileUri= profile.getProfileImg();
        if (profileUri != null && !profileUri.isEmpty())
        {
            //how to load from file path https://www.geeksforgeeks.org/kotlin/how-to-display-image-from-image-file-path-in-android-using-jetpack-compose/
           // profileImg.setImageURI(Uri.parse(profileUri));
            profileImg.setImageBitmap(BitmapFactory.decodeFile(profileUri));
            profileImgUri = profileUri;
            originalImgUri = profileUri;
        }
        else
        {
            profileImg.setImageResource(R.drawable.jeffy);
            profileImgUri = profileUri;
            originalImgUri = profileUri;
        }
    }
    else
    {
        profileUsername.setText(username);
        profilePassword.setText(password);
        profileHeight.setText(height);
        profileSex.setText(sex);
        profileImg.setImageResource(R.drawable.jeffy);
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
            !profileBMI.getText().toString().equals(originalBMI) ||
            !profileImgUri.equals(originalImgUri))
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

    // try catch was added because it let me add letters and stuff and that would have meesssed up data entry and I didnt wanted to find out how to only accept numbers so toast to the rescue-AV

    try {
        height = Integer.parseInt(profileHeight.getText().toString().trim());
        weight = Integer.parseInt(profileWeight.getText().toString().trim());
    } catch (NumberFormatException e) {
        Toast.makeText(this, "height and text must be whole numbers", Toast.LENGTH_SHORT).show();
        return;
    }

    //moved to repository had to create dummy numbers for the profile constructor
    //DBHelper dbHelper = new DBHelper(this);

    //boolean userUpdated = dbHelper.updateProfile(userID, username, password, sex, height);
    //boolean weightUpdated = dbHelper.saveWeight(userID, weight);
    //boolean imageUpdated = dbHelper.updateProfileImg(userID,profileImgUri);

    profileRepository = new ProfileRepository(this);
    double bmi = 21;
    String uri = "JeffyMishimaBigBuff";
    Profile profile = new Profile(userID, username, password, sex, height, weight, bmi, uri);

    boolean userUpdated = profileRepository.updateUser(profile);
    boolean weightUpdated = profileRepository.updateWeight(userID, weight);
    boolean imageUpdated = profileRepository.updateProfileImg(userID, profileImgUri);

    if (userUpdated && weightUpdated && imageUpdated) {
        bmi = 0;
        if (height > 0 && weight > 0) {
        }

        loadProfileData(userID);
        saveProfile.setEnabled(false);

        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
    }

}

//resource part of code taken from location where media was captured https://developer.android.com/training/data-storage/shared/media
// input output strea, https://www.geeksforgeeks.org/java/java-io-input-output-in-java-with-examples
private String saveImg (Uri photoUri)
{
    try {
        InputStream stream = getContentResolver().openInputStream(photoUri);
        if (stream == null)
        {
            return null;
        }

        //https://www.youtube.com/watch?v=ZtUAfoHHQMo&list=PLF77J-3i-EGkXaWzYphvW6TZYgT6tUD-l
        //https://www.youtube.com/watch?v=OFzrYr_vVWg
        String mine = getContentResolver().getType(photoUri);
        String extension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mine);


        //https://developer.android.com/training/data-storage/app-specific
        File imgDirectory = new File(getFilesDir(), "profile_pictures");
        if(!imgDirectory.exists())
        {
            imgDirectory.mkdir();
        }

        // apparently mime types should be used to put the extension file of the thing to be saved https://stackoverflow.com/questions/23385520/android-available-mime-types
        //atomic counter ^^ https://www.geeksforgeeks.org/java/java-program-to-create-a-file-with-a-unique-name/

        String fileName = "profpic" + counter.incrementAndGet() +"." + extension;
        File imageFile = new File(imgDirectory, fileName);

        OutputStream outputStream = new FileOutputStream(imageFile);

        int temp;
        while ((
                temp = stream.read())
                != -1)
                outputStream.write((byte) temp);



        stream.close();
        outputStream.close();

        return imageFile.getAbsolutePath();



    }
    catch(Exception e)
    {
        System.out.println("the flow of calamity has struck prilfile pic");
        return null;
    }

}

}
