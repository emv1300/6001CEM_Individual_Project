package com.example.a6001cem_artapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DailyChallengeMain extends AppCompatActivity {

    private Button goBack, makePostBT, viewPostBT;
    private TextView challengeWordTV;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge_main);
        goBack = findViewById(R.id.goBackDailyMainBT);
        makePostBT = findViewById(R.id.makePostBT);
        viewPostBT = findViewById(R.id.viewPostsBT);
        challengeWordTV = findViewById(R.id.dailyWordTV);

        dbRef = FirebaseDatabase.getInstance().getReference("Word");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String challenge = (String) snapshot.getValue().toString();
                challengeWordTV.setText(challenge);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeMain.this, HomePage.class);
                startActivity(intent);
            }
        });
        makePostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeMain.this, DailyChallengeUploadPost.class);
                startActivity(intent);
            }
        });
        viewPostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeMain.this, DailyChallengeViewPosts.class);
                startActivity(intent);
            }
        });
    }
}