package com.example.a6001cem_artapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserGuides extends AppCompatActivity {

    private String userGuide;
    private TextView userGuideTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guides);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        userGuideTV = findViewById(R.id.dailyChallengeUserGuideTv);
        Intent intent = getIntent();
        userGuide = ""+ intent.getStringExtra("UserGuide");


        dbRef.child("UserGuide").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (userGuide.equals("show daily guide")){
                    String dailyGuideText = snapshot.child("dailyChallenge").getValue().toString();
                    if (dailyGuideText!=null){
                    userGuideTV.setText(dailyGuideText);
                    }
                }else if (userGuide.equals("show random guide")){
                    String randomGuideText = snapshot.child("randomChallenge").getValue().toString();
                    if (randomGuideText!=null) {
                        userGuideTV.setText(randomGuideText);
                    }
                }else if (userGuide.equals("show settings guide")){
                    String settingsGuideText = snapshot.child("settingsGuide").getValue().toString();
                    if (settingsGuideText!=null) {
                        userGuideTV.setText(settingsGuideText);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}