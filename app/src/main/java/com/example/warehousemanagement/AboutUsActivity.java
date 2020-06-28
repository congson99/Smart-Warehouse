package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutUsActivity extends AppCompatActivity {

    WebView weather_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //Anh xa
        weather_view = (WebView) findViewById(R.id.about_webview);

        weather_view.setWebViewClient(new WebViewClient());
        weather_view.loadUrl("https://www.youtube.com/watch?v=ioNng23DkIM");

        WebSettings webSettings = weather_view.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onBackPressed() {
        if(weather_view.canGoBack()){
            weather_view.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}