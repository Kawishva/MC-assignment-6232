package com.example.mc_assignment_6232;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class list_data_activity extends AppCompatActivity {

    private ListView listView;
    private ImageButton backBtn;
    private List<user_data> userList;
    private MyAdapter myAdapter;
    private AppDatabase appDatabase;
    private Button pushToCloudBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        backBtn = findViewById(R.id.backBtn);
        pushToCloudBtn = findViewById(R.id.pushToCould);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.listview);
        appDatabase = AppDatabase.getInstance(this);


        userList = new ArrayList<>();

        pushToCloudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToCloud();
            }

            private void pushToCloud() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (!userList.isEmpty()) {
                    String userId = currentUser.getUid();
                    for (user_data user : userList) {
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", user.getName());
                        userData.put("age", user.getAge());
                        userData.put("gender", user.getGender());
                        userData.put("image", user.getImage());

                        db.collection("Users").document(userId).collection("UserData").add(userData)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }

                    Toast.makeText(list_data_activity.this, "All data pushed to cloud..", Toast.LENGTH_SHORT).show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            appDatabase.userDao().deleteAll();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    userList.clear();
                                    myAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(list_data_activity.this, "Please enter user data first..", Toast.LENGTH_SHORT).show();
                }
            }
        });





        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });





        new Thread(new Runnable() {
            @Override
            public void run() {
                userList = appDatabase.userDao().getAllUsers();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Set the adapter for the list view
                        myAdapter = new MyAdapter(list_data_activity.this, userList);
                        listView.setAdapter(myAdapter);
                    }
                });
            }
        }).start();
    }


    private class MyAdapter extends BaseAdapter {
        private List<user_data> userList;
        private LayoutInflater layoutInflater;

        public MyAdapter(Context context, List<user_data> userList) {
            this.userList = userList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_user, null);
                holder = new ViewHolder();
                holder.listImage = convertView.findViewById(R.id.listImage);
                holder.listName = convertView.findViewById(R.id.listName);
                holder.listAge = convertView.findViewById(R.id.listAge);
                holder.listGender = convertView.findViewById(R.id.listGender);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            user_data user = userList.get(position);
            holder.listName.setText(user.getName());
            holder.listAge.setText(String.valueOf(user.getAge()));
            holder.listGender.setText(user.getGender());


            if (user.getImage() != null) {
                byte[] imageBytes = Base64.decode(user.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.listImage.setImageBitmap(bitmap);
            } else {
                holder.listImage.setImageResource(R.drawable.ic_baseline_person_24);
            }

            return convertView;
        }
    }


    private static class ViewHolder {
        ImageView listImage;
        TextView listName;
        TextView listAge;
        TextView listGender;
    }
}
