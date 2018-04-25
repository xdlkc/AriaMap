package com.xidian.aria.ariamap;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    @Override
    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_assets/subway.html");

        searchSub = findViewById(R.id.searchSub);

        searchSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                webView.addJavascriptInterface(new JSSubway(), "android");
            }
        });

    }

    private final class JSSubway {
        @SuppressLint("JavascriptInterface")
        public String getCityName() {
            EditText subwayEdittext = findViewById(R.id.subwayEdittext);
            String subwayCity = subwayEdittext.getText().toString();
            return subwayCity;
        }

    }
}
