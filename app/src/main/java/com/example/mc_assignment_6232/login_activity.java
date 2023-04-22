package com.example.mc_assignment_6232;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_activity extends AppCompatActivity {

    EditText login_email,login_password;
    TextView registerbtn;
    Button loginbtn;
    FirebaseAuth mAuth;
    SharedPreferences preferences;
    int attemps=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        loginbtn  = findViewById(R.id.login_button);
        registerbtn = findViewById(R.id.login_reg_button);
        preferences = getSharedPreferences("User_data",MODE_PRIVATE);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register_activity.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email,password;

                email = String.valueOf(login_email.getText());
                password = String.valueOf(login_password.getText());
                String saved_password = preferences.getString("password", "");




                if(TextUtils.isEmpty(email)){
                    Toast.makeText(login_activity.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(login_activity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    Toast.makeText(login_activity.this,"Enter Email and Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        Toast.makeText(login_activity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }

                                    if (!password.equals(saved_password)) {

                                            attemps++;


                                            if(attemps ==3){
                                                finishAffinity();
                                            }
                                            login_password.setText("");
                                            Toast.makeText(getApplicationContext(), "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();




                                            return;
                                        }


                                    else {
                                            Toast.makeText(login_activity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }


                            });
                }
            }
        });
    }
}