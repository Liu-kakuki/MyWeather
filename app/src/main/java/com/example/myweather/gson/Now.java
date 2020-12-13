package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    //实况观测时间
    @SerializedName("obsTime")
    public String obsTime;
    //实况温度，默认单位：摄氏度
    @SerializedName("temp")
    public String temp;
    //实况体感温度，默认单位：摄氏度
    @SerializedName("feelsLike")
    public String feelsLike;
    //当前天气状况和图标的代码，图标可通过天气状况和图标下载
    @SerializedName("icon")
    public String icon;
    //实况天气状况的文字描述，包括阴晴雨雪等天气状态的描述
    @SerializedName("text")
    public String text;
    //实况风向360角度
    @SerializedName("wind360")
    public String wind360;
    //实况风向
    @SerializedName("windDir")
    public String windDir;
    //实况风力等级
    @SerializedName("windScale")
    public String windScale;
    //实况风速，公里/小时
    @SerializedName("windSpeed")
    public String windSpeed;
    //实况相对湿度，百分比数值
    @SerializedName("humidity")
    public String humidity;
    //实况降水量，默认单位：毫米
    @SerializedName("precip")
    public String precip;
    //实况大气压强，默认单位：百帕
    @SerializedName("pressure")
    public String pressure;
    //实况能见度，默认单位：公里
    @SerializedName("vis")
    public String vis;
    //实况云量，百分比数值
    @SerializedName("cloud")
    public String cloud;
    //实况露点温度
    @SerializedName("dew")
    public String dew;
}
