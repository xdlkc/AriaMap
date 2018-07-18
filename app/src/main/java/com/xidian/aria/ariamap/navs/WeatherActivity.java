package com.xidian.aria.ariamap.navs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.dao.WeatherDO;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        Future<String > future = service.submit(new WeatherCallable());
        String w= null;
        try {
            w = future.get();
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


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    class WeatherCallable implements Callable<String >{
        private String result;

        @Override
        public String call() throws Exception {
            String url= "http://v.juhe.cn/weather/index?cityname="+city+"&key=xxxx&format=2";
            result = PureNetUtil.get(url);
            return result;//通过工具类获取返回数据
        }
    }
}
