package com.CST8319.canadianfitnesstracker.repository;

import com.CST8319.canadianfitnesstracker.activity.Profile;

public interface RepositoryInterfase {
    Profile getUserId (int userID);
    boolean updateUser(Profile profile);
    boolean updateWeight(int userID, int weight);
    boolean updateProfileImg(int userID, String uri);
}
