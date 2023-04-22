package com.example.mc_assignment_6232;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    Button change_app_color_btn,add_data_btn,list_data_btn;
    int dcolor;
    RelativeLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        change_app_color_btn = findViewById(R.id.color_change_btn);
        add_data_btn = findViewById(R.id.add_data_btn);
        list_data_btn = findViewById(R.id.list_Data_btn);
        main_layout = findViewById(R.id.main_layout);

        dcolor = ContextCompat.getColor(MainActivity.this, R.color.buttonColor);

        change_app_color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_color_picker();
            }
        });


        add_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), add_data_activity.class);
                startActivity(intent2);
                finish();
            }
        });

       list_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), list_data_activity.class);
                startActivity(intent3);
                finish();
            }
        });
    }

    public void open_color_picker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, dcolor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                dcolor = color;
                main_layout.setBackgroundColor(dcolor);
            }
        });
        ambilWarnaDialog.show();
    }
}