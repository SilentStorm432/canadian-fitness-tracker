package com.CST8319.canadianfitnesstracker.repository;

import android.content.Context;

import com.CST8319.canadianfitnesstracker.activity.Profile;
import com.CST8319.canadianfitnesstracker.database.DBHelper;

public class ProfileRepository implements RepositoryInterfase {

    private DBHelper dbHelper;

    public ProfileRepository(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    @Override
    public Profile getUserId(int userID)
    {
        return dbHelper.getUserByID(userID);
    }

    @Override
    public boolean updateUser(Profile profile)
    {
        int height = Integer.parseInt(profile.getHeight());
        return dbHelper.updateProfile(
                profile.getUserID(),
                profile.getUsername(),
                profile.getPassword(),
                profile.getSex(),
                height
        );

    }

    @Override
    public boolean updateWeight(int userID, int weight)
    {
        return dbHelper.saveWeight( userID, weight);

    }

    @Override
    public boolean updateProfileImg(int userID, String uri)
    {
        return dbHelper.updateProfileImg(userID, uri);
    }

}
