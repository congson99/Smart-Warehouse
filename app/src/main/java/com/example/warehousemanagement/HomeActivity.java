package com.example.warehousemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button bt_en;
    Button bt_we;
    Button bt_av;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Intent from Login
        Intent intentf = getIntent();
        final String id = intentf.getStringExtra("id");

        //Anh xa
        bt_en = (Button) findViewById(R.id.home_bt_enviroment);
        bt_we = (Button) findViewById(R.id.home_bt_weather);
        bt_av = (Button) findViewById(R.id.home_bt_avatar);

        //Enviroment_button
        bt_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to Enviroment
                Intent intent = new Intent(HomeActivity.this, EnvironmentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //Weather_button
        bt_we.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to Weather
                Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //Avatar_button
        bt_av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to Weather
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}