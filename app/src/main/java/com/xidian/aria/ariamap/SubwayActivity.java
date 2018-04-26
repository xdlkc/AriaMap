package com.xidian.aria.ariamap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SubwayActivity extends AppCompatActivity {
    private Button searchSub;
    private WebView webView;
    String city = null;

    @Override
    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);
        webView = findViewById(R.id.webview);
        webView.loadUrl("file:///android_assets/subway.html");
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());

        webView.addJavascriptInterface(new JSClass(), "android");
        searchSub = findViewById(R.id.searchSub);

        searchSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent intent = getIntent();
//                city = intent.getStringExtra("city");
                webView.loadUrl("javascript:showSubway()" );
            }
        });


    }

    class JSClass{
        @JavascriptInterface
        public void showAndroid(){
            Toast.makeText(SubwayActivity.this,"js调用了android的方法",Toast.LENGTH_SHORT).show();
        }
    }
}
