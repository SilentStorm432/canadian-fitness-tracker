package com.CST8319.canadianfitnesstracker.activity;

public class Profile {

    private int userID;
    private String usernamme;
    private String password;
    private String sex;
    private int height;
    private int weight;
    private double BMI;
    private String profileImg;

    public Profile(int userID, String username, String password, String sex, int height, int weight, double BMI, String profileImg)
    {
        this.userID = userID;
        this.usernamme = username;
        this.password = password;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.BMI = BMI;
        this.profileImg = profileImg;
    }


    public int getUserID() { return userID; }
    public String getUsername() { return usernamme; }
    public String getPassword() { return password; }
    public String getSex() { return sex; }
    public String getHeight() { return String.valueOf(height); }
    public String getWeight() { return String.valueOf(weight); }
    public String getBMI() { return String.format("%.2f", BMI); }
    public String getProfileImg() {return profileImg;}

}
