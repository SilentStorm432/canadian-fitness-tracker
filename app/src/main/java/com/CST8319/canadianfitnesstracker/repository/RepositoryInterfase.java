package com.CST8319.canadianfitnesstracker.repository;

import com.CST8319.canadianfitnesstracker.activity.Profile;

public interface RepositoryInterfase {
    Profile getUserId (int userID);
    boolean updateUser(Profile profile);
    boolean updateWeight(int userID, int weight);
    //remove from interface and make it a profilerepository only method -av
    boolean updateProfileImg(int userID, String uri);
}
