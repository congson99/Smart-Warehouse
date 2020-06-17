package com.example.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    WebView weather_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //list weather_links
        final String[] locationsarray = { "https://www.accuweather.com/vi/vn/hanoi/353412/daily-weather-forecast/353412",    //0 Ha Noi
                                    "https://www.accuweather.com/vi/vn/ho-chi-minh-city/353981/weather-forecast/353981",     //1 Ho Chi Minh
                                    "https://www.accuweather.com/vi/vn/da-nang/352954/weather-forecast/352954",              //2 Da Nang
                                    "https://www.accuweather.com/vi/vn/haiphong/353511/weather-forecast/353511",             //3 Hai Phong
                                    "https://www.accuweather.com/vi/vn/can-tho/352508/weather-forecast/352508",              //4 Can Tho
                                    "https://www.accuweather.com/vi/vn/ha-long/355736/weather-forecast/355736",              //5 Ha Long
                                    "https://www.accuweather.com/vi/vn/thanh-hoa/356184/weather-forecast/356184",            //6 Thanh Hoa
                                    "https://www.accuweather.com/vi/vn/hue/356204/weather-forecast/356204",                  //7 Hue
                                    "https://www.accuweather.com/vi/vn/nha-trang/354222/weather-forecast/354222",            //8 Nha Trang
                                    "https://www.accuweather.com/vi/vn/pleiku/353265/weather-forecast/353265",               //9 Pleiku
                                    "https://www.accuweather.com/vi/vn/thu-dau-mot/352246/weather-forecast/352246"};         //10 Thu Dau 1

        //intent from home
        Intent intentf = getIntent();
        final String id = intentf.getStringExtra("id");

        //Anh xa
        weather_view = (WebView) findViewById(R.id.weather_webview);

        //get location id
        databaseReference = FirebaseDatabase.getInstance().getReference("Account").child("Location").child("Location");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //set link
                weather_view.setWebViewClient(new WebViewClient());
                weather_view.loadUrl(locationsarray[Integer.parseInt(dataSnapshot.getValue().toString())]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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