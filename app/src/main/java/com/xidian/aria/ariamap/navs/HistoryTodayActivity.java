package com.xidian.aria.ariamap.navs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xidian.aria.ariamap.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 历史上的今天模块
 */
public class HistoryTodayActivity extends AppCompatActivity {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY ="65bb96268ec7cc8ce81f3b8972db5ce9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_today);
        getRequest1();
    }
    //1.事件列表
    public static void getRequest1(){
        Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        String year=calendar.get(Calendar.YEAR)+"";
        String month=calendar.get(Calendar.MONTH)+1+"";
        String day=calendar.get(Calendar.DAY_OF_MONTH)+"";
        String hour=calendar.get(Calendar.HOUR_OF_DAY)+"";
        String minute=calendar.get(Calendar.MINUTE)+"";
        String second=calendar.get(Calendar.SECOND)+"";
        String time=year+month+day+hour+minute+second;
        String result =null;
        String url ="http://api.juheapi.com/japi/toh";//请求接口地址
        HashMap<String ,String > params = new HashMap<>();//请求参数
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("v","1.0");//版本，当前：1.0
        params.put("month",month);//月份，如：10
        params.put("day",day);//日，如：1

        try {
            result =net(url, params, "GET");
            System.out.println(result);
//            JSONObject object = JSONObject.fromObject(result);
//            if(object.getInt("error_code")==0){
//                System.out.println(object.get("result"));
//            }else{
//                System.out.println(object.get("error_code")+":"+object.get("reason"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.根据ID查询事件详情
    public static void getRequest2(){
        String result =null;
        String url ="http://api.juheapi.com/japi/tohdet";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("v","");//版本，当前：1.0
        params.put("id","");//事件ID

//        try {
//            result =net(url, params, "GET");
//
//            JSONObject object = JSONObject.w(result);
//            if(object.getInt("error_code")==0){
//                System.out.println(object.get("result"));
//            }else{
//                System.out.println(object.get("error_code")+":"+object.get("reason"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
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
    public static String urlencode(Map<String,Object>data) {
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
