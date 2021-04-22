package com.example.a6001cem_artapp.randomChallenges;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.a6001cem_artapp.HomePage;
import com.example.a6001cem_artapp.R;
import com.example.a6001cem_artapp.UserGuides;

public class ChallengeNavigation extends AppCompatActivity {
    private ImageButton landscapeChallenge, charDChallenge, geomChallenge, userGuideBT, colourChallenge;
    private Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_navigation);
        goBack = findViewById(R.id.goBackChallengeSelectionBT);
        landscapeChallenge = findViewById(R.id.challenge1IBT);
        charDChallenge = findViewById(R.id.challenge2IBT);
        geomChallenge = findViewById(R.id.challenge3IBT);
        colourChallenge = findViewById(R.id.challenge4IBT);

        userGuideBT = findViewById(R.id.RandomChallengeUserGuideIBT);
        userGuideBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ChallengeNavigation.this, UserGuides.class);
                intent.putExtra("UserGuide", "show random guide");
                startActivity(intent);
            }
        });

        landscapeChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ChallengeNavigation.this, ChallengeLandscape.class);
                startActivity(intent);
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ChallengeNavigation.this, HomePage.class);
                startActivity(intent);
            }
        });

        charDChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeNavigation.this, ChallengeCharacterDesign.class);
                startActivity(intent);
            }
        });
        geomChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeNavigation.this, Challenge3DGeometry.class);
                startActivity(intent);
            }
        });
        colourChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChallengeNavigation.this, ChallengeColours.class);
                startActivity(intent);
            }
        });
    }
}