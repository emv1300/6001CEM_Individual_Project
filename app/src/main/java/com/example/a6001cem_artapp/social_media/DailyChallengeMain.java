package com.example.a6001cem_artapp.social_media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a6001cem_artapp.HomePage;
import com.example.a6001cem_artapp.R;
import com.example.a6001cem_artapp.UserGuides;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DailyChallengeMain extends AppCompatActivity {

    private Button goBack, makePostBT, viewPostBT, userGuideBT;
    private TextView challengeWordTV;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_challenge_main);
        goBack = findViewById(R.id.goBackChallengeMainBT);
        makePostBT = findViewById(R.id.makeChallengePostBT);
        viewPostBT = findViewById(R.id.viewChallengePostsBT);
        challengeWordTV = findViewById(R.id.dailyWordTV);
        userGuideBT = findViewById(R.id.dailyChallengeUserGuideBT);
        userGuideBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(DailyChallengeMain.this, UserGuides.class);
                intent.putExtra("UserGuide", "show daily guide");
                startActivity(intent);
            }
        });

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
                intent.putExtra("is_challenge","challenge");
                startActivity(intent);
            }
        });
        viewPostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyChallengeMain.this, DailyChallengeViewPosts.class);
                intent.putExtra("viewPosts", "viewChallengePosts");
                startActivity(intent);
            }
        });
    }
}