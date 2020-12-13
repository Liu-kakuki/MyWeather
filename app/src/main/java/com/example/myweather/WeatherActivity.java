package com.example.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myweather.gson.Weather;
import com.example.myweather.service.AutoUpdateService;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.Utility;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView tempText;
    private TextView feelsLikeText;
    private TextView textText;
    private TextView wind360Text;
    private TextView windDirText;
    private TextView windScaleText;
    private TextView windSpeedText;
    private TextView humidityText;
    private TextView precipText;
    private TextView pressureText;
    private TextView visText;
    private TextView cloudText;
    private ImageView icoImage;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    public String countyName;
    public DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        tempText = findViewById(R.id.temp_text);
        feelsLikeText = findViewById(R.id.feelsLike_text);
        textText = findViewById(R.id.text_text);
        wind360Text = findViewById(R.id.wind360_text);
        windDirText = findViewById(R.id.windDir_text);
        windScaleText = findViewById(R.id.windScale_text);
        windSpeedText = findViewById(R.id.windSpeed_text);
        humidityText = findViewById(R.id.humidity_text);
        precipText = findViewById(R.id.precip_text);
        pressureText = findViewById(R.id.pressure_text);
        visText = findViewById(R.id.vis_text);
        cloudText = findViewById(R.id.cloud_text);
        icoImage = findViewById(R.id.ico_Image);
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //调用DrawerLayout的openDrawer() 方法来打开滑动菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = prefs.getString("weatherId",null);
            countyName = prefs.getString("countyName",null);
            showWeatherInfo(weather,countyName);
        } else {
            // 无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");
            countyName = getIntent().getStringExtra("countyName");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
            editor.putString("countyName", countyName);
            editor.apply();
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId,countyName);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                countyName = prefs.getString("countyName",null);
                requestWeather(mWeatherId,countyName);
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId,final String countyName) {
        String weatherUrl = "https://devapi.qweather.com/v7/weather/now?location=" + weatherId + "&key=28272b57f3bb48739a029f45e7d90e74";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "200".equals(weather.code)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weatherId",weatherId);
                            editor.putString("countyName", countyName);
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weatherId;
                            showWeatherInfo(weather,countyName);
                            Toast.makeText(WeatherActivity.this, "获取天气信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    public int  getResource(String imageName){
        Class drawable = R.drawable.class;
        try {
            Field field = drawable.getField("ic_"+imageName);
            int resId = field.getInt(imageName);
            return resId;
        } catch (NoSuchFieldException e) {
            return 0;
        } catch (IllegalAccessException e) {
            return 0;
        }
    }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather,String countyName) {
        titleCity.setText(countyName);
        titleUpdateTime.setText(weather.updateTime);
        if(weather.now !=null){
            icoImage.setImageResource(getResource(weather.now.icon));
            tempText.setText(weather.now.temp+"°C");
            feelsLikeText.setText(weather.now.feelsLike+"°C");
            textText.setText(weather.now.text);
            wind360Text.setText(weather.now.wind360+"度");
            windDirText.setText(weather.now.windDir);
            windScaleText.setText(weather.now.windScale);
            windSpeedText.setText(weather.now.windSpeed+"km/h");
            humidityText.setText(weather.now.humidity+"%");
            precipText.setText(weather.now.precip+"mm");
            pressureText.setText(weather.now.pressure+"hPa");
            visText.setText(weather.now.vis+"km");
            cloudText.setText(weather.now.cloud+"%");
        }
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}

