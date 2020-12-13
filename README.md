学号：2018211466		姓名：刘远荣		班级：2018211313

## Android大作业——天气app

### 一、 开发背景

《第一行代码 Android》这本教材里面有一个实战例子，开发酷派天气，我学完之后尝试自己也做一个天气app，实际上手之后发现教材里面的api接口、数据处理、依赖包已经过时，加上 android.support 中结构过于混乱，Google 已推出 AndroidX 来代替 support 包，Google不再对 support 提供更新支持等问题，程序已经无法运行，所以知识是前进的，我通过克服这些困难最终完成了最后的作业，在碰壁的过程中也学到了很多知识，以及如何通过查阅资料自行解决。

### 二、开发的意义

完善教材中无法运行的代码，通过查错在错误中学习，在学习教材的代码的时候学会规范的书写代码。

程序可以实现查询中国地区每个县级地区的天气情况，天气情况可以自己手动更新、也可以在后台自动更新（每8h更新一次）

### 三、开发过程

**3.1在[和风天气](https://id.qweather.com/#/homepage)网站注册一个账号，取得一个Web API接口的KEY，并阅读开发文档，了解API接口的使用**

![image-20201213160954232](https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213161001.png)

![image-20201213161141216](https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213161141.png)

**3.2 获取天气数据**

通过

~~~
https://devapi.qweather.com/v7/weather/now?location=城市代号&key=KEY
~~~

例如下面的例子，即可获得一串JSON数据，通过解析JSON数据即可知道天气情况。

~~~
https://devapi.qweather.com/v7/weather/now?location=CN101190401&key=28272b57f3bb48739a029f45e7d90e74
~~~

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213161539.png" alt="image-20201213161539729" style="zoom: 80%;" />

**3.3 分析可得，需要的库有：OkHttp进行网络请求、GSON解析JSON数据、城市信息需要用到数据库存储，需要LitePal，下拉刷新需要swiperefreshlayout，导入依赖包**

app/bulid.gradle

~~~
dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.1'
    implementation 'org.litepal:LitePal:1.2.0'
    implementation 'com.squareup.okhttp3:okhttp:4.10.0-RC1'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.github.bumptech.glide:glide:4.11.0'
    implementation 'com.baoyz.pullrefreshlayout:library:1.2.0'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-alpha03'
    testImplementation 'junit:junit:4.13.1'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation 'com.baoyz.pullrefreshlayout:library:1.2.0'
}
~~~

3.4 项目目录

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213162411.png" alt="image-20201213162411171" style="zoom:80%;" />

**db包（数据库，依赖LitePal库，数据继承DataSuport）：**

**Province.class存储省份数据，City.class存储市数据，County.class存储县数据**
Province.class

City.class

County.class

**还需新建一个assets/litepal.xml声明数据库的存在，每次更新数据litepal.xml文件version就+1**

![image-20201213164100264](https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213164100.png)

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<litepal>
    <dbname value="myweather" />
    <version value="3" />
    <list>
        <mapping class="com.example.myweather.db.Province" />
        <mapping class="com.example.myweather.db.City" />
        <mapping class="com.example.myweather.db.County" />
    </list>
</litepal>
~~~

**json包（处理返回的JSON数据，依赖于GSON库）：**

**分析返回的JSON数据数据，创建一个GSON实体类Now.class解析“now”里面的数据，创建一个GSON实体类Weather.class解析整个JSON数据**

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213163419.png" alt="image-20201213163419881" style="zoom:80%;" />

Weather.class

Now.class

**service包（更新服务）：**

**当有数据缓存的时候，每八小时更新一次天气。**

AutoUpdateService.class

**util包（处理工具）：**

**HttpUtil.class处理http请求，Util处理服务器返回的省级、市级、县级数据，将返回的JSON数据解析成Weather实体类。**

HttpUtil.class

Util.class

**MainActivity.class判断是否有缓存数据，如果有缓存数据则切换到天气情况页面，如果没有缓存数据则显示城市选择的主界面**

MainActivity.class

**ChooseAreaFragment.class读取数据库的城市信息并展示出来，通过监控点击的行号判断、跳转到下一级地区**

ChooseAreaFragment.class

**通过ChooseAreaFragment.class知道要查询的地区之后，WeatherActivity.class负责向服务器发起请求数据，把数据缓存下来，并且将数据展示出来**

WeatherActivity.class

**文件布局部分**

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213165805.png" alt="image-20201213165805898" style="zoom:80%;" />

**activity_weather.xml是主体部分，activity_weather.xml把其他的布局文件包含进去，利于布局**

**androidx.drawerlayout.widget.DrawerLayout可以实现侧边栏功能**

**androidx.swiperefreshlayout.widget.SwipeRefreshLayout可以实现下拉刷新功能**

**ScrollView支持滑动，以列表形式布局其他文件**

![image-20201213170243734](https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213170243.png)

activity_main.xml

activity_weather.xml

choose_area.xml

ico_layout.xml

other_layout.xml

temp_layout.xml

title.xml

wind_layout.xml

**其他的注意事项：**

![image-20201213171017615](https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213171017.png)

AndroidManifest.xml

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myweather">
    <uses-permission android:name="android.permission.INTERNET" />
    <application
        android:name="org.litepal.LitePalApplication"
        android:allowBackup="true"
        android:icon="@mipmap/my_ico"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"
        android:roundIcon="@mipmap/my_ico"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".WeatherActivity" />
        <service
            android:name=".service.AutoUpdateService"
            android:enabled="true"
            android:exported="true" />
    </application>
</manifest>
~~~

network_security_config.xml

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
~~~

### 四、程序的功能、使用方法

**程序可以获取中国各县级的天气情况，可以自己下拉更新，也可以等程序每八小时自动更新，左上角的侧边栏可以回到选择地区界面**

**下拉即可更新天气信息，天气信息包括温度类（温度、体感温度、天气情况）、风向风力类（风向、风速、风力等级）、其他类**

**天气图标会根据天气情况自动更改。**

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213172351.png" alt="Stitch_20201213_171840" style="zoom: 80%;" />

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213172731.png" alt="Stitch_20201213_172702"  />

<img src="https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213172840.png" alt="Screenshot_20201213-171637" style="zoom: 25%;" />

### 五、实验总结、收获、经验

- 懂得了API接口的调用，程序结果面向现实网络，而不是一个简单的布局

- 由于 android.support 中结构过于混乱，Google 已推出 AndroidX 来代替 support 包，Google不再对 support 提供更新支持，所以不能使用

  ~~~
  android.support.v4.widget.SwipeRefreshLayout
  ~~~

  ~~~
  android.support.v4.widget.DrawerLayout
  ~~~

  而要使用

  ~~~
  androidx.swiperefreshlayout.widget.SwipeRefreshLayout
  ~~~

  ~~~
  androidx.drawerlayout.widget.DrawerLayout
  ~~~

- 联网的要求增加了

  ![image-20201213174908814](https://cdn.jsdelivr.net/gh/Liu-kakuki/Image/20201213174908.png)

- 懂得了JSON数据的处理,在GSON中利用@SerializedName("")可以自动匹配和解析数据

- 切实体验了litePal和SharedPreferences两个数据库的使用，由于省份等信息繁多可以用litePal存储，像天气信息、当前城市的名称、当前城市的ID等少量的信息可以用SharedPreferences存储，由于返回的JSON数据中已经不含城市的信息，所以需要用到SharedPreferences轻量化的存储当前城市的信息。

- 利用反射机制处理图片资源，实现根据图片的名字获取对应的资源ID，方便工具天气情况更改图片。

  ~~~java
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
  ~~~

  
