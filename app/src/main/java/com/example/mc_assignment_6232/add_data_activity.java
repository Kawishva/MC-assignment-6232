package com.example.mc_assignment_6232;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class add_data_activity extends AppCompatActivity {

    ImageButton back_btn;
    EditText user_name,user_age;
    ImageView user_image;
    Button image_btn,save_btn;
    RadioGroup user_gender;
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        user_name = findViewById(R.id.user_name);
        user_age = findViewById(R.id.user_age);
        user_image = findViewById(R.id.user_image);
        user_gender = findViewById(R.id.user_gender);
        image_btn = findViewById(R.id.image_btn);
        save_btn = findViewById(R.id.save_btn);
        back_btn = findViewById(R.id.back_btn);

        db = AppDatabase.getInstance(getApplicationContext());
        user_image.setImageDrawable(null);

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(add_data_activity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_name,u_age,u_gender;
                Drawable u_image;


                u_name = String.valueOf(user_name.getText());
                u_age = user_age.getText().toString();
                u_gender = user_gender.getCheckedRadioButtonId() == R.id.male_selector ? "Male" : "Female";


                if(TextUtils.isEmpty(u_name)){
                    Toast.makeText(add_data_activity.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(u_age)){
                    Toast.makeText(add_data_activity.this,"Enter Age",Toast.LENGTH_SHORT).show();
                    return;
                }


                int checkedRadioButtonId = user_gender.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(add_data_activity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(user_image.getDrawable() == null) {
                    Toast.makeText(add_data_activity.this,"Upload a Image",Toast.LENGTH_SHORT).show();
                    return;
                }


                else {

                    u_image = user_image.getDrawable();
                    int user_Age = Integer.parseInt(u_age);
                    Bitmap u_image_bitmap = ((BitmapDrawable) u_image).getBitmap();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    u_image_bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String encoded_u_image_string = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    user_data user = new user_data(u_name,user_Age,u_gender,encoded_u_image_string);
                    saveUserData(user);


                    user_name.setText("");
                    user_age.setText("");
                    user_gender.clearCheck();
                    user_image.setImageDrawable(null);


                }



            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        user_image.setImageURI(uri);
    }

    private void saveUserData(user_data user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.userDao().insertUser(user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(add_data_activity.this, "Saved....", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        }).start();
    }



}