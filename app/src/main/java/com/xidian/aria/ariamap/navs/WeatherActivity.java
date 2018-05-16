package com.xidian.aria.ariamap.navs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.dao.WeatherDO;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherActivity extends AppCompatActivity {
    String city;
    // 实时天气
    TextView tempTv;
    TextView windDirectionTv;
    TextView windStrengthTv;
    TextView humidityTv;
    TextView timeTv;

    // 今日天气
    TextView cityTv;
    TextView date_yTv;
    TextView temperatureTv;
    TextView weatherTv;
    TextView windTv;
    TextView dressing_indexTv;
    TextView dressing_adviceTv;
    TextView uv_indexTv;
    TextView comfort_indexTv;
    TextView wash_indexTv;
    TextView travel_indexTv;
    TextView exercise_indexTv;
    TextView drying_indexTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tempTv = findViewById(R.id.weather_now_temp);
        windDirectionTv = findViewById(R.id.weather_now_wind_direction);
        windStrengthTv = findViewById(R.id.weather_now_wind_strength);
        humidityTv = findViewById(R.id.weather_now_humidity);
        timeTv = findViewById(R.id.weather_now_time);
        cityTv = findViewById(R.id.weather_today_city);
        date_yTv = findViewById(R.id.weather_today_date_y);
        temperatureTv = findViewById(R.id.weather_today_temperature);
        weatherTv = findViewById(R.id.weather_today_weather);
        windTv = findViewById(R.id.weather_today_wind);
        dressing_indexTv = findViewById(R.id.weather_today_dressing_index);
        dressing_adviceTv =findViewById(R.id.weather_today_dressing_advice);
        uv_indexTv =findViewById(R.id.weather_today_uv_index);
        comfort_indexTv = findViewById(R.id.weather_today_comfort_index);
        wash_indexTv = findViewById(R.id.weather_today_wash_index);
        travel_indexTv = findViewById(R.id.weather_today_travel_index);
        exercise_indexTv = findViewById(R.id.weather_today_exercise_index);
        drying_indexTv = findViewById(R.id.weather_today_drying_index);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        ExecutorService service = Executors.newSingleThreadExecutor();
//        Future<String > future = service.submit(new WeatherCallable());
        //            String w=future.get();
        String w = "{\"resultcode\":\"200\",\"reason\":\"successed!\",\"result\":{\"sk\":{\"temp\":\"24\",\"wind_direction\":\"北风\",\"wind_strength\":\"1级\",\"humidity\":\"71%\",\"time\":\"00:38\"},\"today\":{\"temperature\":\"21℃~35℃\",\"weather\":\"晴转阴\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"02\"},\"wind\":\"西南风微风\",\"week\":\"星期三\",\"city\":\"西安\",\"date_y\":\"2018年05月16日\",\"dressing_index\":\"炎热\",\"dressing_advice\":\"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。\",\"uv_index\":\"强\",\"comfort_index\":\"\",\"wash_index\":\"较适宜\",\"travel_index\":\"较适宜\",\"exercise_index\":\"较适宜\",\"drying_index\":\"\"},\"future\":[{\"temperature\":\"21℃~35℃\",\"weather\":\"晴转阴\",\"weather_id\":{\"fa\":\"00\",\"fb\":\"02\"},\"wind\":\"西南风微风\",\"week\":\"星期三\",\"date\":\"20180516\"},{\"temperature\":\"21℃~34℃\",\"weather\":\"阴转多云\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"01\"},\"wind\":\"西风3-5级\",\"week\":\"星期四\",\"date\":\"20180517\"},{\"temperature\":\"18℃~30℃\",\"weather\":\"小雨\",\"weather_id\":{\"fa\":\"07\",\"fb\":\"07\"},\"wind\":\"东北风3-5级\",\"week\":\"星期五\",\"date\":\"20180518\"},{\"temperature\":\"16℃~26℃\",\"weather\":\"小雨转阴\",\"weather_id\":{\"fa\":\"07\",\"fb\":\"02\"},\"wind\":\"东北风微风\",\"week\":\"星期六\",\"date\":\"20180519\"},{\"temperature\":\"18℃~27℃\",\"weather\":\"阴转多云\",\"weather_id\":{\"fa\":\"02\",\"fb\":\"01\"},\"wind\":\"东北风微风\",\"week\":\"星期日\",\"date\":\"20180520\"},{\"temperature\":\"16℃~26℃\",\"weather\":\"小雨转阴\",\"weather_id\":{\"fa\":\"07\",\"fb\":\"02\"},\"wind\":\"东北风微风\",\"week\":\"星期一\",\"date\":\"20180521\"},{\"temperature\":\"18℃~30℃\",\"weather\":\"小雨\",\"weather_id\":{\"fa\":\"07\",\"fb\":\"07\"},\"wind\":\"东北风3-5级\",\"week\":\"星期二\",\"date\":\"20180522\"}]},\"error_code\":0}";
        Gson gson = new Gson();
        WeatherDO weather = gson.fromJson(w,WeatherDO.class);
            WeatherDO.WeatherV2 result = weather.getResult();
            WeatherDO.WeatherNow sk = result.getSk();
            WeatherDO.WeatherToday today = result.getToday();
            tempTv.setText("温度："+sk.getTemp());
            windDirectionTv.setText("风向："+sk.getWind_direction());
            windStrengthTv.setText("风力："+sk.getWind_strength());
            humidityTv.setText("湿度："+sk.getHumidity());
            timeTv.setText("更新时间："+sk.getTime());

            cityTv.setText("城市："+today.getCity());
            date_yTv.setText("日期："+today.getDate_y());
            temperatureTv.setText("温度："+today.getTemperature());
            weatherTv.setText("天气"+today.getWeather());
            windTv.setText("风向："+today.getWind());
            dressing_indexTv.setText("穿衣指数："+today.getDressing_index());
            dressing_adviceTv.setText("穿衣建议："+today.getDressing_advice());
            uv_indexTv.setText("紫外线强度："+today.getUv_index());
            comfort_indexTv.setText("舒适度指数："+today.getComfort_index());
            wash_indexTv.setText("洗车指数："+today.getWash_index());
            travel_indexTv.setText("旅游指数："+today.getTravel_index());
            exercise_indexTv.setText("晨炼指数："+today.getExercise_index());
            drying_indexTv.setText("干燥指数："+today.getDrying_index());
    }
    class WeatherCallable implements Callable<String >{
        private String result;

        @Override
        public String call() throws Exception {
            String url= "http://v.juhe.cn/weather/index?cityname="+city+"&key=d116d41b9f197d2391a91afa9a89d100&format=2";
            result = PureNetUtil.get(url);
            return result;//通过工具类获取返回数据
        }
    }
}
