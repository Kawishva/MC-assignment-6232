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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register_activity extends AppCompatActivity {

    EditText register_email,register_password;
    TextView loginbtn;
    Button registerbtn;
    FirebaseAuth mAuth;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        registerbtn = findViewById(R.id.register_button);
        loginbtn = findViewById(R.id.register_log_button);
        preferences = getSharedPreferences("User_register_data", MODE_PRIVATE);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login_activity.class);
                startActivity(intent);
                finish();
            }
        });


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email,password;
                email = String.valueOf(register_email.getText());
                password = String.valueOf(register_password.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(register_activity.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(register_activity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    Toast.makeText(register_activity.this,"Enter Email and Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("User_Email", email);
                                        editor.putString("User_Password", password);
                                        editor.apply();

                                        Toast.makeText(register_activity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent_main =new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent_main);
                                        finish();

                                    } else {


                                        Toast.makeText(register_activity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }





            }
        });

    }
}