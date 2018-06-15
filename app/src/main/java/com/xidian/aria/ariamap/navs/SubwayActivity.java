package com.xidian.aria.ariamap.navs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xidian.aria.ariamap.R;

/**
 * 地铁线路图
 */
public class SubwayActivity extends AppCompatActivity {
    private Button searchSub;
    private WebView webView;
    private EditText editText;
    String city = null;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);
        webView = findViewById(R.id.webview);
        searchSub = findViewById(R.id.search_subway_btn);
        editText = findViewById(R.id.subway_edt);
        WebSettings settings = webView.getSettings();
        webView.setWebChromeClient(new WebChromeClient());
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/subway.html");
        searchSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = editText.getText().toString();
                if (city.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入城市名！",Toast.LENGTH_LONG).show();
                    return;
                }
                String s = "javascript:search_by_city('"+city+"')";
                webView.loadUrl(s);
            }
        });


    }

}
