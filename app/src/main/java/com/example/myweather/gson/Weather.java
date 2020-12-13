package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("code")
    public String code;
    @SerializedName("updateTime")
    public String updateTime;
    public Now now;
}
