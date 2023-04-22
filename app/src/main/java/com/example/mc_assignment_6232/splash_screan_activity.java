package com.example.mc_assignment_6232;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splash_screan_activity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screan);

        Handler handler = new Handler();


        preferences = getSharedPreferences("User_register_data",MODE_PRIVATE);

        if(preferences.contains("User_Email")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(splash_screan_activity.this, login_activity.class));
                    finish();
                }
            },4000);
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(splash_screan_activity.this, register_activity.class));
                    finish();
                }
            },4000);
        }




    }
}