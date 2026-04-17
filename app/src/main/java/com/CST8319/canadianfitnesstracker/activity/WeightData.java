package com.CST8319.canadianfitnesstracker.activity;

public class WeightData
{
    private int weightAvg;
    private String date;


    public WeightData(int weightAvg, String date)
    {
        this.weightAvg = weightAvg;
        this.date = date;
    }

    public int getweightAvg() { return weightAvg; }
    public String getdate() { return date; }
}
