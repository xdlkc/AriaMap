package com.xidian.aria.ariamap.navs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xidian.aria.ariamap.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 历史上的今天模块
 */
public class HistoryTodayActivity extends AppCompatActivity {
    private static final String TAG = HistoryTodayActivity.class.getSimpleName();
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private ListView historyLv;
    private ArrayAdapter<String > arrayAdapter;
    private List<String > data;
    private TextView dateTv;
    //配置您申请的KEY
    public static final String APPKEY ="65bb96268ec7cc8ce81f3b8972db5ce9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_today);
        historyLv = findViewById(R.id.history_lv);
        dateTv = findViewById(R.id.history_date_tv);
        Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        String month=calendar.get(Calendar.MONTH)+1+"";
        String day=calendar.get(Calendar.DAY_OF_MONTH)+"";
        String d = "今天是"+month+"月"+day+"日";
        dateTv.setText(d);
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String > future = es.submit(new HistoryCallable());
        data = new ArrayList<>();
        try {
            String result = future.get();
            Gson gson = new Gson();
            HistoryTodayDO todayDO = gson.fromJson(result,HistoryTodayDO.class);
            for (HistoryTodayDO.Detail detail : todayDO.getResult()){
                data.add("日期："+detail.getDate()+"\n标题："+detail.getTitle());
            }
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,data);
            historyLv.setAdapter(arrayAdapter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }


    class HistoryCallable implements Callable<String >{
        private String result;
        @Override
        public String call() throws Exception {
            return getRequest();
        }
        //1.事件列表
        public String getRequest(){
            Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
            String month=calendar.get(Calendar.MONTH)+1+"";
            String day=calendar.get(Calendar.DAY_OF_MONTH)+"";
            result =null;
            String url ="http://v.juhe.cn/todayOnhistory/queryEvent.php";//请求接口地址
            HashMap<String ,String > params = new HashMap<>();//请求参数
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("v","1.0");//版本，当前：1.0
            params.put("date",month+"/"+day);

            try {
                result =net(url, params, "GET");
                Log.d(TAG,"result:"+result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }


        /**
         *
         * @param strUrl 请求地址
         * @param params 请求参数
         * @param method 请求方法
         * @return  网络请求字符串
         * @throws Exception
         */
        public  String net(String strUrl, Map params,String method) throws Exception {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String rs = null;
            try {
                StringBuffer sb = new StringBuffer();
                if(method==null || method.equals("GET")){
                    strUrl = strUrl+"?"+urlencode(params);
                }
                URL url = new URL(strUrl);
                conn = (HttpURLConnection) url.openConnection();
                if(method==null || method.equals("GET")){
                    conn.setRequestMethod("GET");
                }else{
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                }
                conn.setRequestProperty("User-agent", userAgent);
                conn.setUseCaches(false);
                conn.setConnectTimeout(DEF_CONN_TIMEOUT);
                conn.setReadTimeout(DEF_READ_TIMEOUT);
                conn.setInstanceFollowRedirects(false);
                conn.connect();
                if (params!= null && method != null && method.equals("POST")) {
                    try {
                        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                        out.writeBytes(urlencode(params));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                InputStream is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
                String strRead = null;
                while ((strRead = reader.readLine()) != null) {
                    sb.append(strRead);
                }
                rs = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return rs;
        }

        //将map型转为请求参数型
        public  String urlencode(Map<String,Object>data) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry i : data.entrySet()) {
                try {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
}
